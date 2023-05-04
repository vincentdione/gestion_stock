package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.Category;
import jakarta.persistence.OneToMany;
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

    @JsonIgnore
    private List<ArticleDto> articleDtos = new ArrayList<>();


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

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setCode(categoryDto.getCode());
        category.setDesignation(categoryDto.getDesignation());

        return category;
    }


}
