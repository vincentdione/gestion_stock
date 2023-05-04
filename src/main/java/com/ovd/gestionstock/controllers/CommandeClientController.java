package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT)
public interface CommandeClientController {


    @PostMapping(value = "/commandeClients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto request);

    @GetMapping(value = "/commandeClients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandeClientDto>> getAllCommandeClients();

    @GetMapping(value = "/commandeClients/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandeClientDto> getCommandeClientById(@PathVariable("idCommande") Long id);


    @DeleteMapping(value = "/commandeClients/delete/{idCommande}")
    public ResponseEntity deleteCommandeClient(@PathVariable("idCommande") Long id);


}
