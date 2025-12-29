package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.auth.AuthenticationResponse;
import com.ovd.gestionstock.config.JwtService;
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
import com.ovd.gestionstock.token.Token;
import com.ovd.gestionstock.token.TokenRepository;
import com.ovd.gestionstock.token.TokenType;
import com.ovd.gestionstock.validators.UtilisateurValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


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
    @Transactional
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
        tokenRepository.deleteByUtilisateur(utilisateur);
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

    public AuthenticationResponse createUtilisateur(UtilisateurDto request) {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<String> errors = UtilisateurValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error("Erreur, vérifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obligatoires", ErrorCodes.UTILISATEUR_NOT_FOUND);
        }


        // Vérification si un utilisateur avec le même email ou username existe déjà dans cette entreprise
        if (userAlreadyExists(request.getEmail(), request.getUsername(), currentTenant)) {
            throw new InvalidEntityException("Un autre utilisateur avec le même email ou username existe déjà dans cette entreprise",
                    ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                    Collections.singletonList("Email ou username déjà utilisé dans cette entreprise"));
        }

        Entreprise entreprise = entrepriseRepository.findById(currentTenant)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Entreprise non trouvée avec l'ID: " + currentTenant,
                        ErrorCodes.ENTREPRISE_NOT_FOUND));

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .username(request.getUsername())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .entreprise(entreprise)
                .build();

        Utilisateur savedUser = utilisateurRepository.save(utilisateur);

        String accessToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, accessToken);

        String roleName = "ROLE_" + utilisateur.getRole().name();
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .entrepriseNom(entreprise.getNom())
                .roles(Collections.singletonList(roleName))
                .build();
    }

    private void saveUserToken(Utilisateur utilisateur, String jwtToken) {
        var token = Token.builder()
                .utilisateur(utilisateur)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private boolean userAlreadyExists(String email, String username, Long entrepriseId) {
        return utilisateurRepository.existsByEmailAndEntrepriseId(email, entrepriseId)
                || utilisateurRepository.existsByUsernameAndEntrepriseId(username, entrepriseId)
                || utilisateurRepository.existsByEmail(email);
    }


    private void validate(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null || !StringUtils.hasText(dto.getMotDePasse())) {
            throw new InvalidOperationException("Le mot de passe ne peut pas être vide", ErrorCodes.UTILISATEUR_NOT_FOUND);
        }
    }
}
