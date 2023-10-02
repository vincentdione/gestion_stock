package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface ClientController {


    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> saveClient(@RequestBody ClientDto request);

    @GetMapping(value = "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDto>> getAllClients();

    @GetMapping(value = "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> getClientById(@PathVariable("idClient") Long idClient);


    @DeleteMapping(value = "/clients/delete/{idClient}")
    public ResponseEntity deleteClient(@PathVariable("idClient") Long idClient);

    @GetMapping("/search")
    public ResponseEntity<List<Client>> rechercherClients( @RequestParam(required = false) String nom,
                                                           @RequestParam(required = false) String prenom,
                                                           @RequestParam(required = false) String email,
                                                           @RequestParam(required = false) String numTel);


    }
