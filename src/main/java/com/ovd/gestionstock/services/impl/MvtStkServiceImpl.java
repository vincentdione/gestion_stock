package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.ArticleStockStatsDto;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.TypeMvtStk;
import com.ovd.gestionstock.models.MvtStk;
import com.ovd.gestionstock.repositories.MvtStkRepository;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.MvtStkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

    private final MvtStkRepository mvtStkRepository;
    private final ArticleService articleService;
    private final TenantContext tenantContext;
    private final TenantSecurityService tenantSecurityService;

    @Override
    public List<MvtStkDto> getAllMvtStk() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        return mvtStkRepository.findAll().stream()
                .filter(mvt -> currentTenant.equals(mvt.getIdEntreprise()))
                .map(MvtStkDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMvtStk(Long id) {
        if (id == null) {
            log.error("ID mouvement null");
            return;
        }

        Optional<MvtStk> mvtOptional = mvtStkRepository.findById(id);
        if (mvtOptional.isEmpty()) {
            log.warn("Mouvement introuvable pour l'id {}", id);
            return;
        }

        MvtStk mvt = mvtOptional.get();
        try {
            tenantSecurityService.validateAccessToResource(mvt.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        mvtStkRepository.delete(mvt);
    }

    @Override
    public MvtStkDto getMvtStkById(Long id) {
        if (id == null) {
            return null;
        }

        Optional<MvtStk> mvt = mvtStkRepository.findById(id);
        if (mvt.isEmpty()) return null;

        try {
            tenantSecurityService.validateAccessToResource(mvt.get().getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        return MvtStkDto.fromEntity(mvt.get());
    }

    @Override
    public Map<String, BigDecimal> stockReelArticleByUnite(Long idArticle) {
        if (idArticle == null) {
            log.warn("ID article est null");
            return Collections.emptyMap();
        }

        ArticleDto article = articleService.getArticleById(idArticle);
        try {
            tenantSecurityService.validateAccessToResource(article.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        List<Object[]> stockReelByUnite = mvtStkRepository.stockReelArticleByUnite(idArticle);
        Map<String, BigDecimal> stockByUnite = new HashMap<>();
        for (Object[] result : stockReelByUnite) {
            String unite = (String) result[0];
            BigDecimal sumQuantite = (BigDecimal) result[1];
            stockByUnite.put(unite, sumQuantite);
        }

        return stockByUnite;
    }

    @Override
    public BigDecimal stockReelArticle(Long idArticle) {
        if (idArticle == null) {
            return BigDecimal.valueOf(-1);
        }

        ArticleDto article = articleService.getArticleById(idArticle);
        try {
            tenantSecurityService.validateAccessToResource(article.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        return mvtStkRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStockArticle(Long idArticle) {
        ArticleDto article = articleService.getArticleById(idArticle);
        try {
            tenantSecurityService.validateAccessToResource(article.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        return mvtStkRepository.findAllByArticleId(idArticle).stream()
                .map(MvtStkDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MvtStkDto entreeMvtStk(MvtStkDto request) {
        return entreePositive(TypeMvtStk.ENTREE, request);
    }

    @Override
    public MvtStkDto sortieMvtStk(MvtStkDto request) {
        return sortieNegative(request, TypeMvtStk.SORTIE);
    }

    @Override
    public MvtStkDto correctionMvtStkPos(MvtStkDto request) {
        return entreePositive(TypeMvtStk.CORRECTION_POS, request);
    }

    @Override
    public MvtStkDto correctionMvtStkNeg(MvtStkDto request) {
        return sortieNegative(request, TypeMvtStk.CORRECTION_NEG);
    }

    @Override
    public BigDecimal stockVenduArticle(Long idArticle) {
        BigDecimal stockVendu = mvtStkRepository.sumQuantiteByTypeMvtStkAndArticleId(TypeMvtStk.SORTIE, idArticle);
        return stockVendu != null ? stockVendu : BigDecimal.ZERO;
    }

    @Override
    public Map<String, BigDecimal> stockVenduArticleByUnite(Long idArticle, String type) {
        TypeMvtStk typeMvtStk = TypeMvtStk.valueOf(type);
        List<Object[]> stockVenduByUnite = mvtStkRepository
                .sumQuantiteByTypeMvtStkAndArticleIdGroupByUnite(typeMvtStk, idArticle);

        Map<String, BigDecimal> stockVenduMap = new HashMap<>();
        for (Object[] row : stockVenduByUnite) {
            String unite = (String) row[0];
            BigDecimal quantite = (BigDecimal) row[1];
            stockVenduMap.put(unite, quantite);
        }

        return stockVenduMap;
    }

    @Override
    public Map<Long, ArticleStockStatsDto> getArticleStockStats() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<MvtStk> allMouvements = mvtStkRepository.findAllByIdEntreprise(currentTenant);
        Map<Long, ArticleStockStatsDto> statsMap = new HashMap<>();

        for (MvtStk mvt : allMouvements) {
            Long articleId = mvt.getArticle().getId();
            ArticleStockStatsDto stats = statsMap.computeIfAbsent(articleId, id -> {
                ArticleDto article = articleService.getArticleById(id);
                return new ArticleStockStatsDto(
                        articleId,
                        article.getDesignation(),
                        article.getPrixUnitaireHt(),       // Prix d'achat HT
                        article.getPrixUnitaireTtc(),      // Prix de vente TTC
                        BigDecimal.ZERO,                  // Stock restant
                        BigDecimal.ZERO,                   // Quantité vendue
                        BigDecimal.ZERO,                   // Montant total vendu (CA)
                        BigDecimal.ZERO,                   // Montant total acheté
                        BigDecimal.ZERO                     // Bénéfice
                );
            });

            if (mvt.getTypeMvtStk() == TypeMvtStk.ENTREE || mvt.getTypeMvtStk() == TypeMvtStk.CORRECTION_POS) {
                BigDecimal quantite = mvt.getQuantite();
                stats.setStockRestant(stats.getStockRestant().add(quantite));
                // Calcul du coût d'achat pour les entrées
                stats.setMontantTotalAchete(stats.getMontantTotalAchete().add(
                        quantite.multiply(stats.getPrixUnitaireHt())
                ));
            }
            else if (mvt.getTypeMvtStk() == TypeMvtStk.SORTIE) {
                BigDecimal quantiteAbs = mvt.getQuantite().abs();
                stats.setQuantiteVendue(stats.getQuantiteVendue().add(quantiteAbs));

                // Calcul du chiffre d'affaires (prix de vente TTC)
                BigDecimal ca = quantiteAbs.multiply(
                        stats.getPrixUnitaireTtc() != null ?
                                stats.getPrixUnitaireTtc() : stats.getPrixUnitaireHt()
                );
                stats.setMontantTotal(stats.getMontantTotal().add(ca));

                // Calcul du coût des marchandises vendues (CMV)
                BigDecimal cmv = quantiteAbs.multiply(stats.getPrixUnitaireHt());

                // Mise à jour du stock et du bénéfice
                stats.setStockRestant(stats.getStockRestant().subtract(quantiteAbs));
                stats.setBenefice(stats.getMontantTotal().subtract(stats.getMontantTotalAchete()));
            }
            else if (mvt.getTypeMvtStk() == TypeMvtStk.CORRECTION_NEG) {
                stats.setStockRestant(stats.getStockRestant().subtract(mvt.getQuantite().abs()));
            }
        }

        return statsMap;
    }
    private MvtStkDto entreePositive(TypeMvtStk typeMvtStk, MvtStkDto request) {
        List<String> errors = MvtStkValidator.validate(request);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }

        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        request.setQuantite(BigDecimal.valueOf(Math.abs(request.getQuantite().doubleValue())));
        request.setTypeMvtStk(typeMvtStk);
        request.setIdEntreprise(currentTenant);

        return MvtStkDto.fromEntity(mvtStkRepository.save(MvtStkDto.toEntity(request)));
    }

    private MvtStkDto sortieNegative(MvtStkDto dto, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }

        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        dto.setQuantite(BigDecimal.valueOf(Math.abs(dto.getQuantite().doubleValue()) * -1));
        dto.setTypeMvtStk(typeMvtStk);
        dto.setIdEntreprise(currentTenant);

        return MvtStkDto.fromEntity(mvtStkRepository.save(MvtStkDto.toEntity(dto)));
    }
}
