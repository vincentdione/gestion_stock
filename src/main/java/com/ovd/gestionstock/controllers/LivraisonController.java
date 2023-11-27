package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.LivraisonDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface LivraisonController {
    @GetMapping(value = "/livraisons/all", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<List<LivraisonDto>> getAllLivraisons();

    @GetMapping(value = "/livraisons/{idLivraison}", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<LivraisonDto> getLivraisonById(@PathVariable("idLivraison") Long idLivraison);


    @DeleteMapping(value = "/livraisons/delete/{idLivraison}")

    public ResponseEntity deleteLivraison(@PathVariable("idLivraison") Long idLivraison);


}
