package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.FournisseurController;
import com.ovd.gestionstock.criteria.FournisseurSearchCriteria;
import com.ovd.gestionstock.dto.FournisseurDto;
import com.ovd.gestionstock.services.FournisseurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "fournisseurs")
@Slf4j
public class FournisseurApi implements FournisseurController {

    private final FournisseurService fournisseurService;

    @Override
    public ResponseEntity<FournisseurDto> saveFournisseur(FournisseurDto request) {
        return ResponseEntity.ok(fournisseurService.createFournisseur(request));
    }

    @Override
    public ResponseEntity<List<FournisseurDto>> getAllFournisseurs() {
        return ResponseEntity.ok(fournisseurService.getAllFournisseur());
    }

    @Override
    public ResponseEntity<FournisseurDto> getFournisseurById(Long id) {
        return ResponseEntity.ok(fournisseurService.getFournisseurById(id));
    }

    @Override
    public ResponseEntity deleteFournisseur(Long id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<FournisseurDto>> searchFournisseurs(FournisseurSearchCriteria criteria) {
        log.info("Recherche fournisseurs avec critères: {}", criteria);
        return ResponseEntity.ok(fournisseurService.searchFournisseurs(criteria));
    }

    @Override
    public ResponseEntity<Page<FournisseurDto>> searchFournisseursPage(FournisseurSearchCriteria criteria, Pageable pageable) {
        log.info("Recherche paginée fournisseurs avec critères: {}, page: {}", criteria, pageable.getPageNumber());
        return ResponseEntity.ok(fournisseurService.searchFournisseursPage(criteria, pageable));
    }

    @Override
    public ResponseEntity<List<FournisseurDto>> searchFournisseursByText(String searchText) {
        log.info("Recherche rapide fournisseurs avec texte: {}", searchText);
        return ResponseEntity.ok(fournisseurService.searchFournisseursByText(searchText));
    }
}
