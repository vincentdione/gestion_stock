package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.TypeMvtStk;
import com.ovd.gestionstock.repositories.MvtStkRepository;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.services.MvtStkService;
import com.ovd.gestionstock.validators.MvtStkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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

        List<String> errors = MvtStkValidator.validate(request);

        if (!errors.isEmpty()){
            log.error("Article is not valide");
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.ARTICLE_NOT_FOUND,errors);
        }

        request.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(request.getQuantite().doubleValue() ) * -1
                )
        );

        request.setTypeMvtStk(TypeMvtStk.SORTIE);

        return MvtStkDto.fromEntity(mvtStkRepository.save(MvtStkDto.toEntity(request)));
    }

    @Override
    public MvtStkDto correctionMvtStkPos(MvtStkDto request) {
        return entreePositive(TypeMvtStk.CORRECTION_POS,request);
    }

    @Override
    public MvtStkDto correctionMvtStkNeg(MvtStkDto request) {

       return  entreePositive(TypeMvtStk.CORRECTION_NEG,request);
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

}
