package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CommandeClientController;
import com.ovd.gestionstock.criteria.CommandeClientSearchCriteria;
import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.CommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.services.CommandeClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "commandeClients")
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Slf4j
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

    @GetMapping(value = "montant-total-client", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getMontantTotalComClient() {
        List<CommandeClientDto> commandes = commandeClientService.getAllCommandeClient();
        if(!commandes.isEmpty()){
            return ResponseEntity.ok(commandeClientService.getMontantTotalComClient(commandes.stream().map(CommandeClientDto::toEntity).collect(Collectors.toList())));
        }
        else {
            System.out.println("pas de Commandes trouvées !!!");
            return null;
        }
    }

    @GetMapping(value="/search/comClients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeClientDto>> getCommandesByClient(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String codeCommande) {
        return ResponseEntity.ok(commandeClientService.getCommandesByClient(nom, email, codeCommande));
    }

    // =============================================
    // NOUVELLES MÉTHODES DE RECHERCHE AVANCÉE
    // =============================================

    @PostMapping(value = "/commandeClients/search/avancee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recherche avancée de commandes clients avec critères")
    public ResponseEntity<List<CommandeClientDto>> searchCommandesClientAvancee(
            @Parameter(description = "Critères de recherche", required = true)
            @RequestBody CommandeClientSearchCriteria criteria) {
        log.info("Recherche commandes clients avec critères: {}", criteria);
        return ResponseEntity.ok(commandeClientService.searchCommandesClient(criteria));
    }

    @PostMapping(value = "/commandeClients/search/page", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recherche paginée de commandes clients avec critères")
    public ResponseEntity<Page<CommandeClientDto>> searchCommandesClientPage(
            @Parameter(description = "Critères de recherche", required = true)
            @RequestBody CommandeClientSearchCriteria criteria,
            @Parameter(description = "Paramètres de pagination")
            @PageableDefault(size = 20, sort = "dateCommande", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Recherche paginée commandes clients avec critères: {}, page: {}, size: {}",
                criteria, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(commandeClientService.searchCommandesClientPage(criteria, pageable));
    }

    @GetMapping(value = "/commandeClients/search/text", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recherche rapide de commandes clients par texte")
    public ResponseEntity<List<CommandeClientDto>> searchCommandesClientByText(
            @Parameter(description = "Texte de recherche (code, nom client, email, téléphone)")
            @RequestParam(required = false) String searchText) {
        log.info("Recherche rapide commandes clients avec texte: {}", searchText);
        return ResponseEntity.ok(commandeClientService.searchCommandesClientByText(searchText));
    }

}
