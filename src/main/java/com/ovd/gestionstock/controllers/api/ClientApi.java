package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.ClientController;
import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.services.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "clients")
@RequestMapping("/api/v1/admin")

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
}
