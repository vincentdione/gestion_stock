package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT)
public interface VenteController {


    @PostMapping(value = "/ventes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> save(@RequestBody VenteDto request);

    @GetMapping(value = "/ventes/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VenteDto>> getAllVentes();

    @GetMapping(value = "/ventes/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> getVenteById(@PathVariable("idVente") Long id);


    @DeleteMapping(value = "/ventes/delete/{idVente}")
    public ResponseEntity deleteVente(@PathVariable("idVente") Long id);


}