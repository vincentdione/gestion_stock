package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.TypeMvtStk;
import com.ovd.gestionstock.repositories.MvtStkRepository;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.validators.MvtStkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

    private final MvtStkRepository mvtStkRepository;

    private final ArticleService articleService;



    @Override
    public List<MvtStkDto> getAllMvtStk() {
        return mvtStkRepository.findAll().stream().map(MvtStkDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteMvtStk(Long id) {

    }

    @Override
    public MvtStkDto getMvtStkById(Long id) {
        return null;
    }

    @Override
    public Map<String, BigDecimal> stockReelArticleByUnite(Long idArticle) {
        if (idArticle == null) {
            log.warn("ID article est null");
            return Collections.emptyMap();
        }

        ArticleDto article = articleService.getArticleById(idArticle);
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
            log.warn("ID article est null");
            return BigDecimal.valueOf(-1);
        }
        articleService.getArticleById(idArticle);
        return mvtStkRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStockArticle(Long idArticle) {
        return mvtStkRepository.findAllByArticleId(idArticle).stream().map(MvtStkDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public MvtStkDto entreeMvtStk(MvtStkDto request) {

        return   entreePositive(TypeMvtStk.ENTREE,request);
    }

    @Override
    public MvtStkDto sortieMvtStk(MvtStkDto request) {

        return sortieNegative(request, TypeMvtStk.SORTIE);
    }

    @Override
    public MvtStkDto correctionMvtStkPos(MvtStkDto request) {
        return entreePositive(TypeMvtStk.CORRECTION_POS,request);
    }

    @Override
    public MvtStkDto correctionMvtStkNeg(MvtStkDto request) {

       return  sortieNegative(request, TypeMvtStk.CORRECTION_NEG);
    }

    @Override
    public BigDecimal stockVenduArticle(Long idArticle) {
        // Recherche de la somme de la quantité pour les mouvements de stock de type "SORTIE"
        BigDecimal stockVendu = mvtStkRepository.sumQuantiteByTypeMvtStkAndArticleId(TypeMvtStk.SORTIE, idArticle);

        if (stockVendu == null) {
            stockVendu = BigDecimal.ZERO;
        }

        return stockVendu;
    }

    @Override
    public Map<String, BigDecimal> stockVenduArticleByUnite(Long idArticle, String type) {

        TypeMvtStk typeMvtStk = TypeMvtStk.valueOf(type);
        List<Object[]> stockVenduByUnite = mvtStkRepository.sumQuantiteByTypeMvtStkAndArticleIdGroupByUnite(typeMvtStk, idArticle);

        Map<String, BigDecimal> stockVenduMap = new HashMap<>();

        for (Object[] row : stockVenduByUnite) {
            String unite = (String) row[0];
            BigDecimal quantite = (BigDecimal) row[1];

            stockVenduMap.put(unite, quantite);
        }

        return stockVenduMap;
    }

    private MvtStkDto entreePositive(TypeMvtStk typeMvtStk,MvtStkDto request){
        List<String> errors = MvtStkValidator.validate(request);
        if (!errors.isEmpty()){
            log.error("Article is not valide");
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.ARTICLE_NOT_FOUND,errors);
        }

        request.setQuantite(BigDecimal.valueOf(Math.abs(request.getQuantite().doubleValue())));
        request.setTypeMvtStk(typeMvtStk);
        return MvtStkDto.fromEntity(mvtStkRepository.save(MvtStkDto.toEntity(request)));
    }

    private MvtStkDto sortieNegative(MvtStkDto dto, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }

        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue()) * -1
                )
        );
        dto.setTypeMvtStk(typeMvtStk);
        return MvtStkDto.fromEntity(
                mvtStkRepository.save(MvtStkDto.toEntity(dto))
        );
    }

}
