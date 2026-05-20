package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private Long id;
    private String codeArticle;
    private String codeBarre;
    private String designation;
    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;
    private String photo;
    private Long idEntreprise;

    private SousCategoryDto sousCategoryDto;
    private List<UniteDto> unites;

    public static ArticleDto fromEntity(Article article) {
        if (article == null) {
            return null;
        }

        return ArticleDto.builder()
                .id(article.getId())
                .codeArticle(article.getCodeArticle())
                .codeBarre(article.getCodeBarre())
                .designation(article.getDesignation())
                .prixUnitaireHt(article.getPrixUnitaireHt())
                .tauxTval(article.getTauxTval())
                .prixUnitaireTtc(article.getPrixUnitaireTtc())
                .idEntreprise(article.getIdEntreprise())
                .sousCategoryDto(article.getSousCategory() != null ?
                        SousCategoryDto.fromEntity(article.getSousCategory()) : null)
                .unites(article.getConditions() != null ?
                        article.getConditions().stream()
                                .map(cond -> UniteDto.fromEntity(cond.getUnite()))
                                .collect(Collectors.toList())
                        : null)
                .photo(article.getPhoto())
                .build();
    }

    public static Article toEntity(ArticleDto articleDto) {
        if (articleDto == null) {
            return null;
        }

        Article article = Article.builder()
                .id(articleDto.getId())
                .codeArticle(articleDto.getCodeArticle())
                .codeBarre(articleDto.getCodeBarre())
                .designation(articleDto.getDesignation())
                .prixUnitaireHt(articleDto.getPrixUnitaireHt())
                .tauxTval(articleDto.getTauxTval())
                .prixUnitaireTtc(articleDto.getPrixUnitaireTtc())
                .idEntreprise(articleDto.getIdEntreprise())
                .photo(articleDto.getPhoto())
                .build();

        // Gérer la sous-catégorie séparément pour éviter les problèmes de référence
        if (articleDto.getSousCategoryDto() != null) {
            article.setSousCategory(SousCategoryDto.toEntity(articleDto.getSousCategoryDto()));
        }

        return article;
    }

    // Méthode builder statique pour l'importation
    public static ArticleDto importBuilder(String codeArticle, String designation,
                                           BigDecimal prixUnitaireHt, BigDecimal tauxTval,
                                           BigDecimal prixUnitaireTtc, String photo,
                                           SousCategoryDto sousCategoryDto, Long idEntreprise) {
        return ArticleDto.builder()
                .codeArticle(codeArticle)
                .designation(designation)
                .prixUnitaireHt(prixUnitaireHt)
                .tauxTval(tauxTval)
                .prixUnitaireTtc(prixUnitaireTtc)
                .photo(photo)
                .sousCategoryDto(sousCategoryDto)
                .idEntreprise(idEntreprise)
                .build();
    }
}