package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MvtStkDto {

    private Long id;
    private Instant dateMvt;
    private BigDecimal quantite;

    private ArticleDto article;

    private TypeMvtStk typeMvtStk;

    private SourceMvt sourceMvt;

    private Long idEntreprise;
    private String unite;




    public static MvtStkDto fromEntity(MvtStk mvtStk){
        if(mvtStk == null){
            return null;
        }
        return MvtStkDto.builder()
                .id(mvtStk.getId())
                .dateMvt(mvtStk.getDateMvt())
                .quantite(mvtStk.getQuantite())
                .typeMvtStk(mvtStk.getTypeMvtStk())
                .sourceMvt(mvtStk.getSourceMvt())
                .article(ArticleDto.fromEntity(mvtStk.getArticle()))
                .idEntreprise(mvtStk.getIdEntreprise())
                .unite(mvtStk.getUnite())
                .build();
    }

    public static MvtStk toEntity(MvtStkDto mvtStkDto){
        if(mvtStkDto == null){
            return null;
        }
        return MvtStk.builder()
                .id(mvtStkDto.getId())
                .dateMvt(mvtStkDto.getDateMvt())
                .quantite(mvtStkDto.getQuantite())
                .typeMvtStk(mvtStkDto.getTypeMvtStk())
                .article(ArticleDto.toEntity(mvtStkDto.getArticle()))
                .sourceMvt(mvtStkDto.getSourceMvt())
                .idEntreprise(mvtStkDto.getIdEntreprise())
                .unite(mvtStkDto.getUnite())
                .build();
    }

}
