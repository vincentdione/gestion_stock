package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ChangerMotDePasseUtilisateurDto;
import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.exceptions.InvalidOperationException;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.EntrepriseRepository;
import com.ovd.gestionstock.repositories.UtilisateurRepository;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.services.UtilisateurEntrepriseService;
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
public class UtilisateurEntrepriseServiceImpl implements UtilisateurEntrepriseService {

    private final UtilisateurRepository utilisateurRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final TenantContext tenantContext;
    private final TenantSecurityService tenantSecurityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UtilisateurDto> getAllUtilisateur() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }
        Optional<Entreprise> entreprise = entrepriseRepository.findById(currentTenant);
        List<Utilisateur> utilisateurs = utilisateurRepository.findByEntreprise(entreprise.get());

        if (utilisateurs.isEmpty()) {
            log.warn("Aucun utilisateur trouvé pour le tenant {}", currentTenant);
        }

        return utilisateurs.stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUtilisateur(Long id) {
        if (id == null) {
            log.error("ID EST NULL");
            return;
        }

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur non trouvé avec l'id: " + id,
                        ErrorCodes.UTILISATEUR_NOT_FOUND));

        Long currentTenant = tenantContext.getCurrentTenant();
        if (!utilisateur.getEntreprise().getId().equals(currentTenant)) {
            throw new RuntimeException("Accès refusé. Vous ne pouvez pas supprimer cet utilisateur.");
        }

        utilisateurRepository.delete(utilisateur);
    }

    @Override
    public UtilisateurDto changerPassword(ChangerMotDePasseUtilisateurDto dto) {
        validate(dto);

        Utilisateur utilisateur = utilisateurRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur non trouvé avec l'id: " + dto.getId(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));

        Long currentTenant = tenantContext.getCurrentTenant();
        if (!utilisateur.getEntreprise().getId().equals(currentTenant)) {
            throw new RuntimeException("Accès refusé. Vous ne pouvez pas modifier le mot de passe de cet utilisateur.");
        }

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

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID = " + id + " n'a été trouvé",
                        ErrorCodes.UTILISATEUR_NOT_FOUND));

        Long currentTenant = tenantContext.getCurrentTenant();
        if (!utilisateur.getEntreprise().getId().equals(currentTenant)) {
            throw new RuntimeException("Accès refusé. Vous ne pouvez pas accéder à cet utilisateur.");
        }

        return UtilisateurDto.fromEntity(utilisateur);
    }

    @Override
    public UtilisateurDto findByUsername(String username) {
        return utilisateurRepository.findByUsername(username)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur non trouvé avec le username: " + username,
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
    }

    @Override
    public UtilisateurDto createUtilisateur(UtilisateurDto request) {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<String> errors = UtilisateurValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error("Erreur, vérifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obligatoires", ErrorCodes.UTILISATEUR_NOT_FOUND);
        }

        if (userAlreadyExists(request.getEmail())) {
            throw new InvalidEntityException("Un autre utilisateur avec le même email existe déjà", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                    Collections.singletonList("Un autre utilisateur avec le même email existe déjà dans la BDD"));
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Utilisateur utilisateur = UtilisateurDto.toEntity(request);
        utilisateur.setEntreprise(entrepriseRepository.findById(currentTenant)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Entreprise non trouvée avec l'ID: " + currentTenant,
                        ErrorCodes.ENTREPRISE_NOT_FOUND)));

        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
    }

    private boolean userAlreadyExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    private void validate(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null || !StringUtils.hasText(dto.getMotDePasse())) {
            throw new InvalidOperationException("Le mot de passe ne peut pas être vide", ErrorCodes.UTILISATEUR_NOT_FOUND);
        }
    }
}
