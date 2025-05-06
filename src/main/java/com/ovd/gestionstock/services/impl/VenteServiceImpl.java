package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.LigneVenteRepository;
import com.ovd.gestionstock.repositories.VenteRepository;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.services.VenteService;
import com.ovd.gestionstock.validators.VenteValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ArticleRepository articleRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final MvtStkService mvtStkService;
    private final JdbcTemplate jdbcTemplate;
    private final TenantContext tenantContext;
    private final TenantSecurityService tenantSecurityService;

    @Override
    public List<VenteDto> getAllVentes() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        return venteRepository.findAll().stream()
                .filter(v -> Objects.equals(v.getIdEntreprise(), currentTenant))
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VenteDto getVentesById(Long id) {
        if (id == null){
            log.error("ID est null");
            return null;
        }

        Ventes ventes = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun vente n'a ete trouve dans la BDD",
                        ErrorCodes.VENTE_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(ventes.getIdEntreprise());
        } catch (Exception e) {
            throw new RuntimeException("Accès refusé à la ressource", e);
        }

        return VenteDto.fromEntity(ventes);
    }

    @Override
    public VenteDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Vente CODE is NULL");
            return null;
        }

        return venteRepository.findVentesByCode(code)
                .filter(v -> Objects.equals(v.getIdEntreprise(), tenantContext.getCurrentTenant()))
                .map(VenteDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente trouvée avec le code " + code,
                        ErrorCodes.VENTE_NOT_VALID));
    }

    @Override
    public VenteDto createVentes(VenteDto dto) {
        List<String> errors = VenteValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();
        dto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a été trouvé");
            } else {
                try {
                    tenantSecurityService.validateAccessToResource(article.get().getIdEntreprise());
                } catch (Exception e) {
                    throw new RuntimeException("Accès refusé à l'article ID " + article.get().getId(), e);
                }
            }
        });

        if (!articleErrors.isEmpty()) {
            throw new InvalidEntityException("Articles invalides", ErrorCodes.VENTE_NOT_VALID, articleErrors);
        }

        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('SEQ_COMMANDE_VENTE')", Long.class);
        String code = "CMD-VEN" + String.format("%07d", nextVal);

        dto.setCode(code);
        dto.setIdEntreprise(tenantContext.getCurrentTenant());

        Ventes savedVentes = venteRepository.save(VenteDto.toEntity(dto));

        if (dto.getLigneVentes() != null) {
            dto.getLigneVentes().forEach(ligneVenteDto -> {
                LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
                ligneVente.setVente(savedVentes);
                ligneVente.setIdEntreprise(savedVentes.getIdEntreprise());
                ligneVenteRepository.save(ligneVente);
                updateMvtStk(ligneVente);
            });
        }

        return VenteDto.fromEntity(savedVentes);
    }

    @Override
    public void deleteVentes(Long id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return;
        }

        Ventes vente = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée", ErrorCodes.VENTE_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(vente.getIdEntreprise());
        } catch (Exception e) {
            throw new RuntimeException("Accès refusé à la vente", e);
        }

        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidEntityException("Impossible de supprimer une vente liée", ErrorCodes.VENTE_ALREADY_IN_USE);
        }

        venteRepository.deleteById(id);
    }

    @Override
    public List<LigneVenteDto> findAllLigneVentesByVenteId(Long id) {
        return ligneVenteRepository.findAllByVenteId(id).stream()
                .filter(lv -> Objects.equals(lv.getIdEntreprise(), tenantContext.getCurrentTenant()))
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getMontantTotalVentes(List<Ventes> ventes) {
        BigDecimal venteTotal = BigDecimal.ZERO;

        for (Ventes vente : ventes) {
            try {
                tenantSecurityService.validateAccessToResource(vente.getIdEntreprise());
            } catch (Exception e) {
                continue; // ignorer les ventes non autorisées
            }

            List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(vente.getId());
            for (LigneVente ligneVente : ligneVentes) {
                if (ligneVente.getPrixUnitaire() != null && ligneVente.getQuantite() != null) {
                    venteTotal = venteTotal.add(ligneVente.getPrixUnitaire().multiply(ligneVente.getQuantite()));
                }
            }
        }

        return venteTotal;
    }

    private void updateMvtStk(LigneVente lig) {
        MvtStkDto mvtStkDto = MvtStkDto.builder()
                .article(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvtStk(TypeMvtStk.SORTIE)
                .sourceMvt(SourceMvt.VENTE)
                .quantite(lig.getQuantite())
                .idEntreprise(lig.getIdEntreprise())
                .unite(lig.getUnite())
                .build();

        mvtStkService.sortieMvtStk(mvtStkDto);
    }
}
