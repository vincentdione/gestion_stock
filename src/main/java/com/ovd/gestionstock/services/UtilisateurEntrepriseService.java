package com.ovd.gestionstock.services;

import com.ovd.gestionstock.auth.AuthenticationResponse;
import com.ovd.gestionstock.dto.ChangerMotDePasseUtilisateurDto;
import com.ovd.gestionstock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurEntrepriseService {

    public List<UtilisateurDto> getAllUtilisateur();
    public void deleteUtilisateur(Long id);

    UtilisateurDto changerPassword (ChangerMotDePasseUtilisateurDto dto);

    public UtilisateurDto getUtilisateurById(Long id);
    public UtilisateurDto findByUsername(String username);

    public AuthenticationResponse createUtilisateur(UtilisateurDto request);
}
