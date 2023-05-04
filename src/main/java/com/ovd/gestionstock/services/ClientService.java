package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.models.Client;

import java.util.List;

public interface ClientService {

    public List<ClientDto> getAllClient();
    public void deleteClient(Long id);

    public ClientDto getClientById(Long id);

    public ClientDto createClient(ClientDto client);
}
