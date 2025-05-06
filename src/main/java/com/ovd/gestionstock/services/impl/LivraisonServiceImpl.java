package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.LivraisonDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.models.Livraison;
import com.ovd.gestionstock.models.LivraisonEtat;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.CommandeClientRepository;
import com.ovd.gestionstock.repositories.LivraisonRepository;
import com.ovd.gestionstock.services.LivraisonService;
import com.ovd.gestionstock.services.TenantSecurityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LivraisonServiceImpl implements LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final CommandeClientRepository commandeClientRepository;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;

    @Override
    public List<LivraisonDto> getAllLivraisons() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        return livraisonRepository.findAll().stream()
                .filter(l -> l.getIdEntreprise().equals(currentTenant))
                .map(LivraisonDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLivraison(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("L'ID de la livraison est null", ErrorCodes.LIVRAISON_NOT_FOUND);
        }

        livraisonRepository.findById(id).ifPresentOrElse(
                livraison -> {
                    try {
                        tenantSecurityService.validateAccessToResource(livraison.getIdEntreprise());
                        if (livraison.getEtat() == LivraisonEtat.EN_COURS) {
                            livraisonRepository.deleteById(id);
                            log.info("Livraison supprimée avec succès : {}", id);
                        } else {
                            log.warn("Impossible de supprimer une livraison non EN_COURS. ID: {}", id);
                        }
                    } catch (AccessDeniedException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    throw new EntityNotFoundException("Livraison non trouvée avec l'ID : " + id, ErrorCodes.LIVRAISON_NOT_FOUND);
                }
        );
    }

    @Override
    public LivraisonDto getLivraisonById(Long id) {
        Objects.requireNonNull(id, "L'ID de la livraison ne peut pas être null");

        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison non trouvée avec l'ID : " + id, ErrorCodes.LIVRAISON_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(livraison.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        return LivraisonDto.fromEntity(livraison);
    }

    @Override
    public LivraisonDto updateStatut(Long id, String etat) {
        Objects.requireNonNull(id, "L'ID de la livraison ne peut pas être null");

        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Livraison non trouvée avec l'ID : " + id, ErrorCodes.LIVRAISON_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(livraison.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        if (livraison.getEtat() == LivraisonEtat.LIVRER) {
            log.warn("La livraison avec l'ID {} est déjà livrée", id);
            return null;
        }

        try {
            LivraisonEtat nouvelEtat = LivraisonEtat.valueOf(etat);
            livraison.setEtat(nouvelEtat);
            return LivraisonDto.fromEntity(livraison);
        } catch (IllegalArgumentException e) {
            log.error("État de livraison non valide : {}", etat);
            return null;
        }
    }

    @Override
    public LivraisonDto affecterLivraison(Long livraisonId) {
        Utilisateur utilisateur = getUtilisateurEnCours();

        Livraison livraison = livraisonRepository.findById(livraisonId)
                .orElseThrow(() -> new EntityNotFoundException("Livraison non trouvée avec l'ID : " + livraisonId, ErrorCodes.LIVRAISON_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(livraison.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        if (livraison.getUtilisateur() == null) {
            livraison.setUtilisateur(utilisateur);
            livraisonRepository.save(livraison);
            return LivraisonDto.fromEntity(livraison);
        } else {
            log.warn("La livraison avec l'ID {} est déjà affectée", livraisonId);
            return null;
        }
    }

    @Override
    public List<LivraisonDto> voirLivraisonsUtilisateurEnCours() {
        Utilisateur utilisateur = getUtilisateurEnCours();
        return livraisonRepository.findByUtilisateur(utilisateur).stream()
                .filter(l -> l.getIdEntreprise().equals(tenantContext.getCurrentTenant()))
                .map(LivraisonDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivraisonDto> getLivraisonsByStatut(String statut) {
        try {
            LivraisonEtat etat = statut != null ? LivraisonEtat.valueOf(statut) : LivraisonEtat.EN_COURS;
            return livraisonRepository.findByEtat(etat).stream()
                    .filter(l -> l.getIdEntreprise().equals(tenantContext.getCurrentTenant()))
                    .map(LivraisonDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.error("Statut de livraison non valide : {}", statut);
            return Collections.emptyList();
        }
    }

    private Utilisateur getUtilisateurEnCours() {
        return (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
