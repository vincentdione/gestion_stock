package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;

import java.util.List;

public interface LigneCommandeFournisseurService {

    public List<LigneCommandeFournisseurDto> getAllLigneCommandeFournisseur();
    public void deleteLigneCommandeFournisseur(Long id);

    public LigneCommandeFournisseurDto getLigneCommandeFournisseurById(Long id);

    public LigneCommandeFournisseurDto createLigneCommandeFournisseur(LigneCommandeFournisseurDto request);
}
