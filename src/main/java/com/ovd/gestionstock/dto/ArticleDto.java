package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    private CategoryDto categoryDto;

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
                .photo(articleDto.getPhoto())
                .build();

    }


}
