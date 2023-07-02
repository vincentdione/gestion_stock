package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.CommandeFournisseurDto;

import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurController {


    @PostMapping(value = "/commandeFournisseurs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> saveCommandeFournisseurs(@RequestBody CommandeFournisseurDto request);

    @PatchMapping(value = "/commandeFournisseurs/updateEtat/{idCommande}/{etatCommande}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> updateEtatCommande(@PathVariable("idCommande") Long idCommande,@PathVariable("etatCommande") CommandeEtat etatCommande);

    @PatchMapping(value = "/commandeFournisseurs/updateFournisseur/{idCommande}/{idFournisseur}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> updateFournisseur(@PathVariable("idCommande") Long idCommande,@PathVariable("idFournisseur") Long idFournisseur);

    @PatchMapping(value = "/commandeFournisseurs/updateQuantite/{idCommande}/{idLigneCommande}/{quantite}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> updateQuantiteCommande(@PathVariable("idCommande") Long idCommande,
                                                                    @PathVariable("idLigneCommande") Long idLigneCommande,
                                                                    @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(value = "/commandeFournisseurs/updateArticle/{idCommande}/{idLigneCommande}/{idArticle}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> updateArticle(@PathVariable("idCommande") Long idCommande,
                                                           @PathVariable("idLigneCommande") Long idLigneCommande,
                                                           @PathVariable("idArticle") Long idArticle);


    @GetMapping(value = "/commandeFournisseurs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeFournisseurDto>> getAllCommandeFournisseurs();

    @GetMapping(value = "/commandeFournisseurs/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> getCommandeFournisseurById(@PathVariable("idCommande") Long idCommande);


    @GetMapping(value = "/commandeFournisseurs/ligneCommande/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeFournisseurDto>> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Long idCommande);



    @DeleteMapping(value = "/commandeFournisseurs/delete/{idCommande}")
    public ResponseEntity<Void> deleteCommandeFournisseur(@PathVariable("idCommande") Long idCommande);


    @DeleteMapping(value = "/commandeFournisseurs/deleteArticle/{idCommande}/{idLigneCommande}")
    ResponseEntity<CommandeFournisseurDto> deleteFournisseurArticle(@PathVariable("idCommande") Long idCommande,@PathVariable("idLigneCommande") Long idLigneCommande);


}
