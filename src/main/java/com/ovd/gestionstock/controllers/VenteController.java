package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT)
@PreAuthorize("hasRole('ADMIN')")
public interface VenteController {


    @PostMapping(value = "/ventes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> saveVente(@RequestBody VenteDto request);

    @GetMapping(value = "/ventes/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VenteDto>> getAllVentes();

    @GetMapping(value = "/ventes/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VenteDto> getVenteById(@PathVariable("idVente") Long id);


    @DeleteMapping(value = "/ventes/delete/{idVente}")
    public ResponseEntity deleteVente(@PathVariable("idVente") Long id);


}
