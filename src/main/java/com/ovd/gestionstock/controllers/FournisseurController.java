package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.FournisseurDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


}
