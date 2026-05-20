package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.criteria.FournisseurSearchCriteria;
import com.ovd.gestionstock.dto.FournisseurDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface FournisseurController {


    @PostMapping(value = "/fournisseurs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FournisseurDto> saveFournisseur(@RequestBody FournisseurDto request);

    @GetMapping(value = "/fournisseurs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FournisseurDto>> getAllFournisseurs();

    @GetMapping(value = "/fournisseurs/{idFournisseur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FournisseurDto> getFournisseurById(@PathVariable("idFournisseur") Long id);


    @DeleteMapping(value = "/fournisseurs/delete/{idFournisseur}")
    public ResponseEntity deleteFournisseur(@PathVariable("idFournisseur") Long id);


    @PostMapping("/fournisseurs/search")
    @Operation(summary = "Recherche avancée de fournisseurs avec critères")
    public ResponseEntity<List<FournisseurDto>> searchFournisseurs(@RequestBody FournisseurSearchCriteria criteria);

    @PostMapping("/fournisseurs/search/page")
    @Operation(summary = "Recherche paginée de fournisseurs avec critères")
    public ResponseEntity<Page<FournisseurDto>> searchFournisseursPage(
            @RequestBody FournisseurSearchCriteria criteria,
            Pageable pageable);

    @GetMapping("/fournisseurs/search/text")
    @Operation(summary = "Recherche rapide de fournisseurs par texte")
    public ResponseEntity<List<FournisseurDto>> searchFournisseursByText(
            @RequestParam(required = false) String searchText);

}
