package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ChangerMotDePasseUtilisateurDto;
import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.exceptions.InvalidOperationException;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.UtilisateurRepository;
import com.ovd.gestionstock.services.UtilisateurService;
import com.ovd.gestionstock.validators.UtilisateurValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    private PasswordEncoder passwordEncoder;


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
    public UtilisateurDto changerPassword(ChangerMotDePasseUtilisateurDto dto) {
        validate(dto);
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());
        if (utilisateurOptional.isEmpty()) {
            log.warn("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId());
            throw new EntityNotFoundException("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId(), ErrorCodes.UTILISATEUR_NOT_FOUND);
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        utilisateur.setPassword(passwordEncoder.encode(dto.getMotDePasse()));

        return UtilisateurDto.fromEntity(
                utilisateurRepository.save(utilisateur)
        );
    }

    @Override
    public UtilisateurDto getUtilisateurById(Long id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return null;
        }
        return utilisateurRepository.findById(id)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
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

        if(userAlreadyExists(request.getEmail())) {
            throw new InvalidEntityException("Un autre utilisateur avec le meme email existe deja", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                    Collections.singletonList("Un autre utilisateur avec le meme email existe deja dans la BDD"));
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Utilisateur utilisateurSaved = utilisateurRepository.save(UtilisateurDto.toEntity(request));
        return UtilisateurDto.fromEntity(utilisateurSaved);
    }


    private boolean userAlreadyExists(String username) {
        Optional<Utilisateur> user = utilisateurRepository.findByUsername(username);
        return user.isPresent();
    }

    private void validate(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null) {
            log.warn("Impossible de modifier le mot de passe avec un objet NULL");
            throw new InvalidOperationException("Aucune information n'a ete fourni pour pouvoir changer le mot de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (dto.getId() == null) {
            log.warn("Impossible de modifier le mot de passe avec un ID NULL");
            throw new InvalidOperationException("ID utilisateur null:: Impossible de modifier le mote de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
            log.warn("Impossible de modifier le mot de passe avec un mot de passe NULL");
            throw new InvalidOperationException("Mot de passe utilisateur null:: Impossible de modifier le mote de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            log.warn("Impossible de modifier le mot de passe avec deux mots de passe different");
            throw new InvalidOperationException("Mots de passe utilisateur non conformes:: Impossible de modifier le mote de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
    }

}
