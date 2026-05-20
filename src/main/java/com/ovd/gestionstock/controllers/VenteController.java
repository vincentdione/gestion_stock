package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.criteria.VenteSearchCriteria;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface VenteController {
    @PostMapping(value = "/ventes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> saveVente(@RequestBody VenteDto request);

    @GetMapping(value = "/ventes/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VenteDto>> getAllVentes();

    @GetMapping(value = "/ventes/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> getVenteById(@PathVariable("idVente") Long id);

    @GetMapping(value = "/ventes/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> getVenteByCode(@PathVariable("code") String code);

    @GetMapping(value = "/ventes/ligneVente/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneVenteDto>> findAllLigneVenteByVenteId(@PathVariable("idVente") Long idVente);

    @DeleteMapping(value = "/ventes/delete/{idVente}")
    public ResponseEntity deleteVente(@PathVariable("idVente") Long id);

    @GetMapping(value = "montant-total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal>  getMontantTotalVentes();

    @PutMapping(value = "/ventes/update/{idVente}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<VenteDto> updateVente(
            @PathVariable("idVente") Long id,
            @RequestBody VenteDto venteDto);

    @PatchMapping(value = "/ventes/{idVente}/client",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<VenteDto> updateClientInfo(
            @PathVariable("idVente") Long id,
            @RequestBody VenteDto venteDto);

    // Nouvel endpoint pour les 10 dernières ventes
    @GetMapping(value = "/ventes/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<VenteDto>> getLatestVentes();


    @PostMapping(value = "/ventes/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<VenteDto>> searchVentes(@RequestBody VenteSearchCriteria criteria);

    // Recherche simple par paramètres GET (alternative)
    @GetMapping(value = "/ventes/search/simple", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<VenteDto>> searchVentesByParams(
            @RequestParam(value = "nomClient", required = false) String nomClient,
            @RequestParam(value = "prenomClient", required = false) String prenomClient,
            @RequestParam(value = "codeVente", required = false) String codeVente,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size);

}
