package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.utils.Constants;
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

    @GetMapping(value = "/ventes/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> getVenteByCode(@PathVariable("code") String code);

    @GetMapping(value = "/ventes/ligneVente/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneVenteDto>> findAllLigneVenteByVenteId(@PathVariable("idVente") Long idVente);


    @DeleteMapping(value = "/ventes/delete/{idVente}")
    public ResponseEntity deleteVente(@PathVariable("idVente") Long id);


    @GetMapping(value = "montant-total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal>  getMontantTotalVentes();
}
