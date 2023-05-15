package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CategoryController;
import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "categories")
public class CategoryApi {

    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping(value = "/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto request) {
        return ResponseEntity.ok(categoryService.createCategory(request));

    }

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(value = "/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getAllCategorys() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping(value = "/categories/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("idCategory") Long idCategory) {
        return ResponseEntity.ok(categoryService.getCategoryById(idCategory));
    }
    @GetMapping(value = "/categories/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategoryByNom(@PathVariable("nom") String nom) {
        return null;
    }

    @DeleteMapping(value = "/categories/delete/{idCategory}")
    public ResponseEntity deleteCategory(@PathVariable("idCategory") Long idCategory) {
        categoryService.deleteCategory(idCategory);
        return ResponseEntity.ok().build();
    }
}
