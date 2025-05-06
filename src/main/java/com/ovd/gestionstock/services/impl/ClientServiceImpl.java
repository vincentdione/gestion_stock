package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.repositories.ClientRepository;
import com.ovd.gestionstock.services.ClientService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.ClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;

    @Override
    @Cacheable("clients")
    public List<ClientDto> getAllClient() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<Client> clients = clientRepository.findAll().stream()
                .filter(client -> client.getIdEntreprise().equals(currentTenant))
                .collect(Collectors.toList());

        return clients.stream().map(ClientDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteClient(Long id) {
        if (id == null) {
            log.error("ID n'existe pas");
            return;
        }

        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            throw new EntityNotFoundException("Client non trouvé", ErrorCodes.CLIENT_NOT_FOUND);
        }

        Client client = optionalClient.get();
        try {
            tenantSecurityService.validateAccessToResource(client.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        clientRepository.delete(client);
    }

    @Override
    public ClientDto getClientById(Long id) {
        if (id == null) {
            log.error("Id n'existe pas !!!");
            return null;
        }

        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            throw new EntityNotFoundException("Client non trouvé", ErrorCodes.CLIENT_NOT_FOUND);
        }

        Client client = optionalClient.get();

        try {
            tenantSecurityService.validateAccessToResource(client.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        return ClientDto.fromEntity(client);
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        List<String> errors = ClientValidator.validate(clientDto);

        if (!errors.isEmpty()) {
            log.error("Erreur vérifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obligatoires", ErrorCodes.CLIENT_NOT_FOUND);
        }

        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        Client client = ClientDto.toEntity(clientDto);
        client.setIdEntreprise(currentTenant);
        Client savedClient = clientRepository.save(client);

        return ClientDto.fromEntity(savedClient);
    }

    @Override
    public List<ClientDto> searchClients(String nom, String prenom, String email, String numTel) {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<Client> clients;

        // Recherche manuelle et filtrage par tenant
        if (nom != null && prenom != null && email != null && numTel != null) {
            clients = clientRepository.findByNomAndPrenomAndEmailAndNumTel(nom, prenom, email, numTel);
        } else if (nom != null && prenom != null) {
            clients = clientRepository.findByNomAndPrenom(nom, prenom);
        } else if (nom != null && email != null) {
            clients = clientRepository.findByNomAndEmail(nom, email);
        } else if (nom != null && numTel != null) {
            clients = clientRepository.findByNomAndNumTel(nom, numTel);
        } else if (email != null) {
            clients = clientRepository.findByEmail(email);
        } else if (numTel != null) {
            clients = clientRepository.findByNumTel(numTel);
        } else {
            clients = clientRepository.findAll();
        }

        // Filtrage tenant
        return clients.stream()
                .filter(client -> client.getIdEntreprise().equals(currentTenant))
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }
}
