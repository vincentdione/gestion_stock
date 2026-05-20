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
public class ConditionAVImportDto {
    private Long id;
    private BigDecimal quantite;
    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;
    private UniteDto unite;
    private ArticleDto article;
    private String codeArticle; // Pour l'importation
    private String uniteNom; // Pour l'importation

    public static ConditionAVImportDto fromEntity(ConditionAV conditionAV) {
        if (conditionAV == null) {
            return null;
        }

        return ConditionAVImportDto.builder()
                .id(conditionAV.getId())
                .prixUnitaireHt(conditionAV.getPrixUnitaireHt())
                .tauxTval(conditionAV.getTauxTval())
                .prixUnitaireTtc(conditionAV.getPrixUnitaireTtc())
                .quantite(conditionAV.getQuantite())
                .unite(UniteDto.fromEntity(conditionAV.getUnite()))
                .article(ArticleDto.fromEntity(conditionAV.getArticle()))
                .codeArticle(conditionAV.getArticle() != null ? conditionAV.getArticle().getCodeArticle() : null)
                .uniteNom(conditionAV.getUnite() != null ? conditionAV.getUnite().getNom() : null)
                .build();
    }

    public static ConditionAV toEntity(ConditionAVImportDto conditionAVDto) {
        if (conditionAVDto == null) {
            return null;
        }

        return ConditionAV.builder()
                .id(conditionAVDto.getId())
                .prixUnitaireHt(conditionAVDto.getPrixUnitaireHt())
                .tauxTval(conditionAVDto.getTauxTval())
                .prixUnitaireTtc(conditionAVDto.getPrixUnitaireTtc())
                .quantite(conditionAVDto.getQuantite())
                .unite(UniteDto.toEntity(conditionAVDto.getUnite()))
                .article(ArticleDto.toEntity(conditionAVDto.getArticle()))
                .build();
    }

    // Builder pour l'importation CSV
    public static ConditionAVImportDto importBuilder(String codeArticle, String uniteNom,
                                               BigDecimal quantite, BigDecimal prixUnitaireHt,
                                               BigDecimal tauxTval, BigDecimal prixUnitaireTtc) {
        return ConditionAVImportDto.builder()
                .codeArticle(codeArticle)
                .uniteNom(uniteNom)
                .quantite(quantite)
                .prixUnitaireHt(prixUnitaireHt)
                .tauxTval(tauxTval)
                .prixUnitaireTtc(prixUnitaireTtc)
                .build();
    }
}
