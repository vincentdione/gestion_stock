package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.Category;
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
public class CategoryDto {

    private Long id;

    private String code;

    private String designation;

//    @JsonIgnore
//    private List<SousCategoryDto> sousCategoryDtos = new ArrayList<>();


    public static CategoryDto fromEntity(Category category) {
        if ( category == null){
            return null;
        }
        // Category <- CategoryDto
        return  CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .designation(category.getDesignation())
                .build();
    }

    public static Category toEntity(CategoryDto categoryDto) {
        if ( categoryDto == null){
            return null;
        }

        Category Category = new Category();
        Category.setId(categoryDto.getId());
        Category.setCode(categoryDto.getCode());
        Category.setDesignation(categoryDto.getDesignation());

        return Category;
    }

}
