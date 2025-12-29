package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.models.Entreprise;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EntrepriseService {

    public List<EntrepriseDto> getAllEntreprise();
    public void deleteEntreprise(Long id);

    public EntrepriseDto getEntrepriseById(Long id);

    EntrepriseDto createEntreprise(EntrepriseDto request);
}
