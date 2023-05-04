package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CategoryController;
import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApi implements CategoryController {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<CategoryDto> save(CategoryDto request) {

        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getAllCategorys() {

        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryById(Long id) {

        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryByNom(String nom) {
        return null;
    }

    @Override
    public ResponseEntity deleteCategory(Long id) {

        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
