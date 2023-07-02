package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientController {


    @PostMapping(value = "/commandeClients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> saveCommandeClients(@RequestBody CommandeClientDto request);

    @PatchMapping(value = "/commandeClients/updateEtat/{idCommande}/{etatCommande}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Long idCommande,@PathVariable("etatCommande") CommandeEtat etatCommande);

    @PatchMapping(value = "/commandeClients/updateClient/{idCommande}/{idClient}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Long idCommande,@PathVariable("idClient") Long idClient);

    @PatchMapping(value = "/commandeClients/updateQuantite/{idCommande}/{idLigneCommande}/{quantite}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Long idCommande,
                                                                    @PathVariable("idLigneCommande") Long idLigneCommande,
                                                                    @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(value = "/commandeClients/updateArticle/{idCommande}/{idLigneCommande}/{idArticle}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Long idCommande,
                                                                    @PathVariable("idLigneCommande") Long idLigneCommande,
                                                                    @PathVariable("idArticle") Long idArticle);


    @GetMapping(value = "/commandeClients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeClientDto>> getAllCommandeClients();

    @GetMapping(value = "/commandeClients/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> getCommandeClientById(@PathVariable("idCommande") Long idCommande);


    @GetMapping(value = "/commandeClients/ligneCommande/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Long idCommande);



    @DeleteMapping(value = "/commandeClients/delete/{idCommande}")
    public ResponseEntity<Void> deleteCommandeClient(@PathVariable("idCommande") Long idCommande);


    @DeleteMapping(value = "/commandeClients/deleteArticle/{idCommande}/{idLigneCommande}")
    ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Long idCommande,@PathVariable("idLigneCommande") Long idLigneCommande);


}
