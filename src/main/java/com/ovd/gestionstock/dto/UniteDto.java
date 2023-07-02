package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.Unite;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniteDto {

    private Long id;
    private String nom;
    private String designation;

    //private ArticleDto article;


    public static UniteDto fromEntity (Unite unite){
        if (unite == null){
            return null;
        }

        return UniteDto.builder()
                .id(unite.getId())
                .nom(unite.getNom())
                .designation(unite.getDesignation())
               // .article(ArticleDto.fromEntity(unite.getArticle()))
                 .build();

    }

    public static Unite toEntity (UniteDto unite){
        if (unite == null){
            return null;
        }

        return Unite.builder()
                .id(unite.getId())
                .nom(unite.getNom())
                .designation(unite.getDesignation())
                //.article((ArticleDto.toEntity(unite.getArticle())))
                .build();

    }

}
