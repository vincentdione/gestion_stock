package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.dto.CommandeFournisseurDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT)
@PreAuthorize("hasRole('ADMIN')")

public interface CommandeFournisseurController {


    @PostMapping(value = "/commandeFournisseurs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> save(@RequestBody CommandeFournisseurDto request);

    @GetMapping(value = "/commandeFournisseurs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeFournisseurDto>> getAllCommandeFournisseurs();

    @GetMapping(value = "/commandeFournisseurs/{idCFournisseur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeFournisseurDto> getCommandeFournisseurById(@PathVariable("idCFournisseur") Long id);


    @DeleteMapping(value = "/commandeFournisseurs/delete/{idCFournisseur}")
    public ResponseEntity deleteCommandeFournisseur(@PathVariable("idCFournisseur") Long id);


}
