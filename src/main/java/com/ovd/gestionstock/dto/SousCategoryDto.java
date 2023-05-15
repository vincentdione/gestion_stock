package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.SousCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SousCategoryDto {

    private Long id;

    private String code;

    private String designation;

    private CategoryDto category;


    public static SousCategoryDto fromEntity(SousCategory sousCategory) {
        if ( sousCategory == null){
            return null;
        }
        // Category <- CategoryDto
        return  SousCategoryDto.builder()
                .id(sousCategory.getId())
                .code(sousCategory.getCode())
                .designation(sousCategory.getDesignation())
                .category(CategoryDto.fromEntity(sousCategory.getCategory()))
                .build();
    }

    public static SousCategory toEntity(SousCategoryDto sousCategoryDto) {
        if ( sousCategoryDto == null){
            return null;
        }

        SousCategory sousCategory = new SousCategory();
        sousCategory.setId(sousCategoryDto.getId());
        sousCategory.setCode(sousCategoryDto.getCode());
        sousCategory.setCategory(CategoryDto.toEntity(sousCategoryDto.getCategory()));
        sousCategory.setDesignation(sousCategoryDto.getDesignation());

        return sousCategory;
    }


}
