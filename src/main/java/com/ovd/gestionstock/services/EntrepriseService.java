package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.models.Entreprise;

import java.util.List;

public interface EntrepriseService {

    public List<EntrepriseDto> getAllEntreprise();
    public void deleteEntreprise(Long id);

    public EntrepriseDto getEntrepriseById(Long id);

    public EntrepriseDto createEntreprise(EntrepriseDto request);
}
