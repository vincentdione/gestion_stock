package com.ovd.gestionstock.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCsvRepresentation {

    @CsvBindByName(column = "codeArticle", required = true)
    private String codeArticle;

    @CsvBindByName(column = "designation", required = true)
    private String designation;

    @CsvBindByName(column = "prixUnitaireHt", required = true)
    private String prixUnitaireHt;

    @CsvBindByName(column = "tauxTval", required = true)
    private String tauxTval;

    @CsvBindByName(column = "prixUnitaireTtc", required = true)
    private String prixUnitaireTtc;

    @CsvBindByName(column = "photo")
    private String photo;

    @CsvBindByName(column = "sousCategoryCode")
    private String sousCategoryCode;
    @CsvBindByName(column = "sousCategoryName")
    private String sousCategoryName;
    @CsvBindByName(column = "categoryCode")
    private String categoryCode;
}