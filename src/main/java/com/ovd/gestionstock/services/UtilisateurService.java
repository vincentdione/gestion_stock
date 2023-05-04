package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.models.Utilisateur;

import java.util.List;

public interface UtilisateurService {

    public List<UtilisateurDto> getAllUtilisateur();
    public void deleteUtilisateur(Long id);

    public UtilisateurDto getUtilisateurById(Long id);

    public UtilisateurDto createUtilisateur(UtilisateurDto request);
}
