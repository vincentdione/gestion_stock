package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.LivraisonDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface LivraisonController {
    @GetMapping(value = "/livraisons/all", produces = MediaType.APPLICATION_JSON_VALUE)

    ResponseEntity<List<LivraisonDto>> getAllLivraisons();

    @GetMapping(value = "/livraisons/{idLivraison}", produces = MediaType.APPLICATION_JSON_VALUE)

    ResponseEntity<LivraisonDto> getLivraisonById(@PathVariable("idLivraison") Long idLivraison);


    @DeleteMapping(value = "/livraisons/delete/{idLivraison}")

    ResponseEntity deleteLivraison(@PathVariable("idLivraison") Long idLivraison);


    @PatchMapping(value = "/livraisons/updateStatut/{idLivraison}/{etat}")
    ResponseEntity<LivraisonDto> updateStatut(@PathVariable("idLivraison") Long idLivraison, @PathVariable("etat") String etat);

    @PatchMapping(value = "/livraisons/affecter/{livraisonId}")
    ResponseEntity<LivraisonDto> affecterLivraison(@PathVariable("livraisonId") Long livraisonId);

    @GetMapping(value = "/livraisons/users/")
    ResponseEntity<List<LivraisonDto>> voirLivraisonsUtilisateurEnCours();

    @GetMapping(value = "/livraisons/statut/{statut}")
    ResponseEntity<List<LivraisonDto>> getLivraisonsByStatut(@PathVariable("statut") String statut);

}
