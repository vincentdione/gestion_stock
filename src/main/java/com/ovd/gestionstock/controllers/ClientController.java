package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT)
public interface ClientController {


    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> save(@RequestBody ClientDto request);

    @GetMapping(value = "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDto>> getAllClients();

    @GetMapping(value = "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> getClientById(@PathVariable("idClient") Long id);


    @DeleteMapping(value = "/clients/delete/{idClient}")
    public ResponseEntity deleteClient(@PathVariable("idClient") Long id);


}
