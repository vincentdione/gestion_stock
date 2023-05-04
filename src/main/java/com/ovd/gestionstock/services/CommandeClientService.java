package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.models.CommandeClient;

import java.util.List;

public interface CommandeClientService {

    public List<CommandeClientDto> getAllCommandeClient();
    public void deleteCommandeClient(Long id);

    public CommandeClientDto getCommandeClientById(Long id);

    public void createCommandeClient(CommandeClientDto request);
}
