package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface EntrepriseController {


    @PostMapping(value = "/entreprises", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntrepriseDto> saveEntreprise(@RequestBody EntrepriseDto request);

    @GetMapping(value = "/entreprises/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EntrepriseDto>> getAllEntreprises();

    @GetMapping(value = "/entreprises/{idEntreprise}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntrepriseDto> getEntrepriseById(@PathVariable("idEntreprise") Long id);


    @DeleteMapping(value = "/entreprises/delete/{idEntreprise}")
    public ResponseEntity deleteEntreprise(@PathVariable("idEntreprise") Long id);


}
