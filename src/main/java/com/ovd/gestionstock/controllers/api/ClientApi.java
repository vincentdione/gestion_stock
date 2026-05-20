package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.criteria.ClientSearchCriteria;
import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.services.ClientService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "clients")
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class ClientApi {

    private final ClientService clientService;

    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> saveClient(@RequestBody ClientDto request) {

        return ResponseEntity.ok(clientService.createClient(request));
    }

    @GetMapping(value = "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDto>> getAllClients() {

        return ResponseEntity.ok(clientService.getAllClient());
    }

    @GetMapping(value = "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> getClientById(@PathVariable("idClient") Long idClient) {

        return  ResponseEntity.ok(clientService.getClientById(idClient));
    }

    @DeleteMapping(value = "/clients/delete/{idClient}")
    public ResponseEntity deleteClient(@PathVariable("idClient") Long idClient) {
            clientService.deleteClient(idClient);
            return ResponseEntity.ok().build();
    }

    @GetMapping(value="/clients/search/simple", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientDto> searchClients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String numTel) {
        return clientService.searchClients(nom, prenom, email, numTel);
    }

    // =============================================
    // NOUVELLES MÉTHODES DE RECHERCHE AVANCÉE
    // =============================================

    @PostMapping(value = "/clients/search/avancee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recherche avancée de clients avec critères")
    public ResponseEntity<List<ClientDto>> searchClientsAvancee(
            @Parameter(description = "Critères de recherche", required = true)
            @RequestBody ClientSearchCriteria criteria) {
        log.info("Recherche clients avec critères: {}", criteria);
        return ResponseEntity.ok(clientService.searchClients(criteria));
    }

    @PostMapping(value = "/clients/search/page", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recherche paginée de clients avec critères")
    public ResponseEntity<Page<ClientDto>> searchClientsPage(
            @Parameter(description = "Critères de recherche", required = true)
            @RequestBody ClientSearchCriteria criteria,
            @Parameter(description = "Paramètres de pagination")
            @PageableDefault(size = 20, sort = "nom", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Recherche paginée clients avec critères: {}, page: {}, size: {}",
                criteria, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(clientService.searchClientsPage(criteria, pageable));
    }

    @GetMapping(value = "/clients/search/text", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Recherche rapide de clients par texte")
    public ResponseEntity<List<ClientDto>> searchClientsByText(
            @Parameter(description = "Texte de recherche (nom, prénom, email, téléphone, adresse)")
            @RequestParam(required = false) String searchText) {
        log.info("Recherche rapide clients avec texte: {}", searchText);
        return ResponseEntity.ok(clientService.searchClientsByText(searchText));
    }


}
