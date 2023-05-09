package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.models.MvtStk;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkService {

     List<MvtStkDto> getAllMvtStk();
     void deleteMvtStk(Long id);

     MvtStkDto getMvtStkById(Long id);

     BigDecimal stockReelArticle(Long idArticle);

     List<MvtStkDto> mvtStockArticle(Long idArticle);
     MvtStkDto entreeMvtStk(MvtStkDto request);
     MvtStkDto sortieMvtStk(MvtStkDto request);
     MvtStkDto correctionMvtStkPos(MvtStkDto request);
     MvtStkDto correctionMvtStkNeg(MvtStkDto request);
}
