package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.Unite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private Long id;
    private String codeArticle;
    private String designation;
    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;
    private String photo;
    private Long idEntreprise;

    private SousCategoryDto sousCategoryDto;
//    private UniteDto unite;

    public static ArticleDto fromEntity (Article article){
        if (article == null){
            return null;
        }

        return ArticleDto.builder()
                .id(article.getId())
                .codeArticle(article.getCodeArticle())
                .designation(article.getDesignation())
                .prixUnitaireHt(article.getPrixUnitaireHt())
                .tauxTval(article.getTauxTval())
                .prixUnitaireTtc(article.getPrixUnitaireTtc())
                .idEntreprise(article.getIdEntreprise())
                .sousCategoryDto(SousCategoryDto.fromEntity(article.getSousCategory()))
//                .unite(UniteDto.fromEntity(article.getUnite()))
                .photo(article.getPhoto())
                .build();
    }

    public static Article toEntity (ArticleDto articleDto){
        if (articleDto == null){
            return null;
        }

        return Article.builder()
                .id(articleDto.getId())
                .codeArticle(articleDto.getCodeArticle())
                .designation(articleDto.getDesignation())
                .prixUnitaireHt(articleDto.getPrixUnitaireHt())
                .tauxTval(articleDto.getTauxTval())
                .prixUnitaireTtc(articleDto.getPrixUnitaireTtc())
                .idEntreprise(articleDto.getIdEntreprise())
                .sousCategory(SousCategoryDto.toEntity(articleDto.getSousCategoryDto()))
//                .unite(UniteDto.toEntity(articleDto.getUnite()))
                .photo(articleDto.getPhoto())
                .build();

    }


}
