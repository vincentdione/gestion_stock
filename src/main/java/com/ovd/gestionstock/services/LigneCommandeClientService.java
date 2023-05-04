package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.models.LigneCommandeClient;

import java.util.List;

public interface LigneCommandeClientService {

    public List<LigneCommandeClientDto> getAllLigneCommandeClient();
    public void deleteLigneCommandeClient(Long id);

    public LigneCommandeClientDto getLigneCommandeClientById(Long id);

    public LigneCommandeClientDto createLigneCommandeClient(LigneCommandeClientDto request);
}
