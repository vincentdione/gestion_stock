package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.models.MvtStk;
import com.ovd.gestionstock.models.TypeMvtStk;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MvtStkService {

     List<MvtStkDto> getAllMvtStk();
     void deleteMvtStk(Long id);

     MvtStkDto getMvtStkById(Long id);

     Map<String, BigDecimal> stockReelArticleByUnite(Long idArticle);

     BigDecimal stockReelArticle(Long idArticle);


     List<MvtStkDto> mvtStockArticle(Long idArticle);
     MvtStkDto entreeMvtStk(MvtStkDto request);
     MvtStkDto sortieMvtStk(MvtStkDto request);
     MvtStkDto correctionMvtStkPos(MvtStkDto request);
     MvtStkDto correctionMvtStkNeg(MvtStkDto request);

     BigDecimal stockVenduArticle(Long idArticle);

     Map<String, BigDecimal> stockVenduArticleByUnite(Long idArticle, String type);


}
