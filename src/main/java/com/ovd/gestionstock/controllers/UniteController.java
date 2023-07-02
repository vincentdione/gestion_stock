package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.UniteDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface UniteController {
    @PostMapping(value = "/unites", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniteDto> saveUnite(@RequestBody UniteDto request);

    @GetMapping(value = "/unites/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UniteDto>> getAllUnites();

    @GetMapping(value = "/unites/{idUnite}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniteDto> getUniteById(@PathVariable("idUnite") Long idUnite);
    @GetMapping(value = "/unites/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UniteDto> getUniteByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = "/unites/delete/{idUnite}")
    public ResponseEntity deleteUnite(@PathVariable("idUnite") Long idUnite);


}
