package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.FournisseurDto;
import com.ovd.gestionstock.models.Fournisseur;

import java.util.List;

public interface FournisseurService {

    public List<FournisseurDto> getAllFournisseur();
    public void deleteFournisseur(Long id);

    public FournisseurDto getFournisseurById(Long id);

    public FournisseurDto createFournisseur(FournisseurDto request);
}
