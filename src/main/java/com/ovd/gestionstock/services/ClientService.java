package com.ovd.gestionstock.services;

import com.ovd.gestionstock.criteria.ClientSearchCriteria;
import com.ovd.gestionstock.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    public List<ClientDto> getAllClient();
    public void deleteClient(Long id);

    public ClientDto getClientById(Long id);

    public ClientDto createClient(ClientDto client);

    List<ClientDto> searchClients(String nom, String prenom, String email, String numTel);
    List<ClientDto> searchClients(ClientSearchCriteria criteria);
    Page<ClientDto> searchClientsPage(ClientSearchCriteria criteria, Pageable pageable);
    List<ClientDto> searchClientsByText(String searchText);
}
