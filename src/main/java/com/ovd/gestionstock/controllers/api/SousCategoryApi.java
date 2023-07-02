package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.SousCategoryController;
import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.services.SousCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "sousCategories")
public class SousCategoryApi implements SousCategoryController {

    private final SousCategoryService sousCategoryService;

    @Override
    public ResponseEntity<SousCategoryDto> saveSousCategory(SousCategoryDto request) {

        return ResponseEntity.ok(sousCategoryService.createSousCategory(request));
    }

    @Override
    public ResponseEntity<List<SousCategoryDto>> getAllSousCategorys() {

        return ResponseEntity.ok(sousCategoryService.getAllSousCategory());
    }

    @Override
    public ResponseEntity<SousCategoryDto> getSousCategoryById(Long id) {

        return ResponseEntity.ok(sousCategoryService.getCategoryById(id));
    }

    @Override
    public ResponseEntity<SousCategoryDto> getSousCategoryByNom(String nom) {
        return null;
    }

    @Override
    public ResponseEntity deleteSousCategory(Long id) {

        sousCategoryService.deleteSousCategory(id);
        return ResponseEntity.ok().build();
    }
}
