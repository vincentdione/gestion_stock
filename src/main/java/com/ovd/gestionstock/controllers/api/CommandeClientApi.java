package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CommandeClientController;
import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.services.CommandeClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommandeClientApi implements CommandeClientController {

    private  final CommandeClientService commandeClientService;

    @Override
    public ResponseEntity<CommandeClientDto> save(CommandeClientDto request) {
        return null;
    }

    @Override
    public ResponseEntity<List<CommandeClientDto>> getAllCommandeClients() {
        return null;
    }

    @Override
    public ResponseEntity<CommandeClientDto> getCommandeClientById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity deleteCommandeClient(Long id) {

        return ResponseEntity.ok().build();
    }
}
