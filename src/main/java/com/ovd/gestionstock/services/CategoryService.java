package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.SousCategoryDto;

import java.util.List;

public interface CategoryService {

    public List<CategoryDto> getAllCategory();
    public void deleteCategory(Long id);

    public CategoryDto getCategoryById(Long id);

    public CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getOrCreateCategory(String code, String defaultName);
}
