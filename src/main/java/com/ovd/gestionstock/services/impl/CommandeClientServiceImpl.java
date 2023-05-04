package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.repositories.CommandeClientRepository;
import com.ovd.gestionstock.services.CommandeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {

    private final CommandeClientRepository commandeClientRepository;
    @Override
    public List<CommandeClientDto> getAllCommandeClient() {
        return null;
    }

    @Override
    public void deleteCommandeClient(Long id) {

    }

    @Override
    public CommandeClientDto getCommandeClientById(Long id) {
        return null;
    }

    @Override
    public void createCommandeClient(CommandeClientDto request) {

    }
}
