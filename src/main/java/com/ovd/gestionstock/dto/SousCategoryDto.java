package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.SousCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SousCategoryDto {

    private Long id;
    private String code;
    private String designation;  // Changé de 'designation' à 'nom' pour cohérence
    private CategoryDto category;
    private String categoryCode;  // Nouveau champ pour l'importation

    public static SousCategoryDto fromEntity(SousCategory sousCategory) {
        if (sousCategory == null) {
            return null;
        }

        return SousCategoryDto.builder()
                .id(sousCategory.getId())
                .code(sousCategory.getCode())
                .designation(sousCategory.getDesignation())
                .category(CategoryDto.fromEntity(sousCategory.getCategory()))
                .categoryCode(sousCategory.getCategory() != null ?
                        sousCategory.getCategory().getCode() : null)
                .build();
    }

    public static SousCategory toEntity(SousCategoryDto sousCategoryDto) {
        if (sousCategoryDto == null) {
            return null;
        }

        return SousCategory.builder()
                .id(sousCategoryDto.getId())
                .code(sousCategoryDto.getCode())
                .designation(sousCategoryDto.getDesignation())
                .category(CategoryDto.toEntity(sousCategoryDto.getCategory()))
                .build();
    }

    // Méthode builder personnalisée pour l'importation
    public static SousCategoryDto importBuilder(String code, String nom, String categoryCode) {
        return SousCategoryDto.builder()
                .code(code)
                .designation(nom)
                .categoryCode(categoryCode)
                .build();
    }
}