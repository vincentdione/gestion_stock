package com.ovd.gestionstock.dto;


import com.ovd.gestionstock.models.ConditionAV;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionAVDto {

    private Long id;


    private BigDecimal quantite;

    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;
    private UniteDto unite;
    private ArticleDto article;

    public static ConditionAVDto fromEntity (ConditionAV conditionAV){
        if (conditionAV == null){
            return null;
        }

        return ConditionAVDto.builder()
                .id(conditionAV.getId())
      //          .quantity(conditionAV.getQuantity())
                .prixUnitaireHt(conditionAV.getPrixUnitaireHt())
                .tauxTval(conditionAV.getTauxTval())
                .prixUnitaireTtc(conditionAV.getPrixUnitaireTtc())
                .quantite(conditionAV.getQuantite())
                .unite(UniteDto.fromEntity(conditionAV.getUnite()))
                .article(ArticleDto.fromEntity(conditionAV.getArticle()))
                .build();
    }

    public static ConditionAV toEntity (ConditionAVDto conditionAV){
        if (conditionAV == null){
            return null;
        }

        return ConditionAV.builder()
                .id(conditionAV.getId())
     //           .quantity(conditionAV.getQuantity())
                .prixUnitaireHt(conditionAV.getPrixUnitaireHt())
                .tauxTval(conditionAV.getTauxTval())
                .prixUnitaireTtc(conditionAV.getPrixUnitaireTtc())
                .quantite(conditionAV.getQuantite())
                .unite(UniteDto.toEntity(conditionAV.getUnite()))
                .article(ArticleDto.toEntity(conditionAV.getArticle()))
                .build();
    }


}
