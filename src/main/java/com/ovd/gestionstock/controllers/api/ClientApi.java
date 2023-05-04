package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.ClientController;
import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientApi implements ClientController {

    private final ClientService clientService;

    @Override
    public ResponseEntity<ClientDto> save(ClientDto request) {

        return ResponseEntity.ok(clientService.createClient(request));
    }

    @Override
    public ResponseEntity<List<ClientDto>> getAllClients() {

        return ResponseEntity.ok(clientService.getAllClient());
    }

    @Override
    public ResponseEntity<ClientDto> getClientById(Long id) {

        return  ResponseEntity.ok(clientService.getClientById(id));
    }


    @Override
    public ResponseEntity deleteClient(Long id) {
            clientService.deleteClient(id);
            return ResponseEntity.ok().build();
    }
}
