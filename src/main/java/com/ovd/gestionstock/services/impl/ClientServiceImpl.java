package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.repositories.ClientRepository;
import com.ovd.gestionstock.services.ClientService;
import com.ovd.gestionstock.validators.ClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Cacheable("clients")
    public List<ClientDto> getAllClient() {
        return clientRepository.findAll().stream().map(ClientDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteClient(Long id) {
        if(id == null){
            log.error("ID n'existe pas");
            return;
        }

        clientRepository.deleteById(id);

    }

    @Override
    public ClientDto getClientById(Long id) {
        if(id == null){
            log.error("Id n'existe pas !!!");
            return null;
        }
        Optional<Client> client = clientRepository.findById(id);
        ClientDto clientDto = ClientDto.fromEntity(client.get());
        return clientDto;
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        List<String> errors = ClientValidator.validate(clientDto);

        if(!errors.isEmpty()){
            log.error("Erreur verifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obliagtoires", ErrorCodes.CLIENT_NOT_FOUND);
        }
        Client savedClient = clientRepository.save(ClientDto.toEntity(clientDto));


        return ClientDto.fromEntity(savedClient);
    }
}
