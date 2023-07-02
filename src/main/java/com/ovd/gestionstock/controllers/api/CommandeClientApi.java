package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CommandeClientController;
import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.services.CommandeClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "commandeClients")
@RequestMapping("/api/v1/admin")
public class CommandeClientApi  {

    private  final CommandeClientService commandeClientService;

    @PostMapping(value = "/commandeClients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> saveCommandeClients(@RequestBody  CommandeClientDto request) {
        commandeClientService.createCommandeClient(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/commandeClients/updateEtat/{idCommande}/{etatCommande}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Long idCommande,@PathVariable("etatCommande") CommandeEtat etatCommande) {
        return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
    }

    @PatchMapping(value = "/commandeClients/updateClient/{idCommande}/{idClient}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Long idCommande,@PathVariable("idClient") Long idClient) {
        return ResponseEntity.ok(commandeClientService.updateClient(idCommande,idClient));
    }

    @PatchMapping(value = "/commandeClients/updateQuantite/{idCommande}/{idLigneCommande}/{quantite}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Long idCommande,
                                                                    @PathVariable("idLigneCommande") Long idLigneCommande,
                                                                    @PathVariable("quantite") BigDecimal quantite) {
        return ResponseEntity.ok(commandeClientService.updateQuantieCommande(idCommande,idLigneCommande,quantite));
    }

    @PatchMapping(value = "/commandeClients/updateArticle/{idCommande}/{idLigneCommande}/{idArticle}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Long idCommande,
                                                           @PathVariable("idLigneCommande") Long idLigneCommande,
                                                           @PathVariable("idArticle") Long idArticle) {
        return ResponseEntity.ok(commandeClientService.updateArticle(idCommande,idLigneCommande,idArticle));
    }

    @GetMapping(value = "/commandeClients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeClientDto>> getAllCommandeClients() {

        return ResponseEntity.ok(commandeClientService.getAllCommandeClient());
    }

    @GetMapping(value = "/commandeClients/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> getCommandeClientById(@PathVariable("idCommande") Long idCommande) {
        return null;
    }

    @GetMapping(value = "/commandeClients/ligneCommande/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Long idCommande) {
        return ResponseEntity.ok(commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande));
    }

    @DeleteMapping(value = "/commandeClients/delete/{idCommande}")
    public ResponseEntity<Void> deleteCommandeClient(@PathVariable("idCommande") Long idCommande) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/commandeClients/deleteArticle/{idCommande}/{idLigneCommande}")
    public ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Long idCommande,@PathVariable("idLigneCommande") Long idLigneCommande) {
        return ResponseEntity.ok(commandeClientService.deleteArticle(idCommande,idLigneCommande));
    }
}
