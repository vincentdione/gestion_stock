package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.UtilisateurRepository;
import com.ovd.gestionstock.services.UtilisateurService;
import com.ovd.gestionstock.validators.UtilisateurValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public List<UtilisateurDto> getAllUtilisateur() {
        log.info("---------------getAllUtilisateur-----------------");
        log.info("---------------getAllUtilisateur-----------------");
        return utilisateurRepository.findAll().stream().map(UtilisateurDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteUtilisateur(Long id) {
        if( id == null){
            log.error("ID EST NULL");
            return;
        }

        utilisateurRepository.deleteById(id);

    }

    @Override
    public UtilisateurDto getUtilisateurById(Long id) {
        if( id == null){
            log.error("ID EST NULL");
            return null;
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);

        return UtilisateurDto.fromEntity(utilisateur.get());
    }

    @Override
    public UtilisateurDto findByUsername(String username) {
        if (!StringUtils.hasLength(username)){
            log.error("Username est null");
        }
         Optional<Utilisateur> utilisateur = utilisateurRepository.findByUsername(username);
        return UtilisateurDto.fromEntity(utilisateur.get());
    }

    @Override
    public UtilisateurDto createUtilisateur(UtilisateurDto request) {
        List<String> errors = UtilisateurValidator.validate(request);

        if (!errors.isEmpty()) {
            log.error("Erreur verifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obligatoires", ErrorCodes.UTILISATEUR_NOT_FOUND);
        }
        Utilisateur utilisateurSaved = utilisateurRepository.save(UtilisateurDto.toEntity(request));
        return UtilisateurDto.fromEntity(utilisateurSaved);
    }
}
