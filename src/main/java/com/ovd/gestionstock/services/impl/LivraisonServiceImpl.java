package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.LivraisonDto;
import com.ovd.gestionstock.models.Livraison;
import com.ovd.gestionstock.models.LivraisonEtat;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.CommandeClientRepository;
import com.ovd.gestionstock.repositories.LivraisonRepository;
import com.ovd.gestionstock.services.LivraisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;


@Service
@RequiredArgsConstructor
@Slf4j
public class LivraisonServiceImpl implements LivraisonService {

    private final LivraisonRepository livraisonRepository;

    private final CommandeClientRepository commandeClientRepository;


    @Override
    public List<LivraisonDto> getAllLivraisons() {
        return livraisonRepository.findAll().stream().map(LivraisonDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteLivraison(Long id) {
        if (id == null){
            log.error("Id Mode Payement est null");
            return;
        }
        Optional<Livraison> livraisonOptional = livraisonRepository.findById(id);
        Livraison livraison = livraisonOptional.get();
        try {
            if(livraison.getEtat().equals(LivraisonEtat.EN_COURS)){
                livraisonRepository.deleteById(id);
            }
            log.info("La livraison avec l'ID {} a été supprimé avec succès", id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Aucune livraison trouvée avec l'ID {}. Aucune suppression effectuée.", id);
        } catch (Exception e) {
            log.error("Une erreur s'est produite lors de la suppression de la livraison avec l'ID " + id, e);
        }
    }

    @Override
    public LivraisonDto getLivraisonById(Long id) {
        // Validation du paramètre
        Objects.requireNonNull(id, "L'ID de la livraison ne peut pas être null");

        Optional<Livraison> findLivraison = livraisonRepository.findById(id);

        if (!findLivraison.isPresent()) {
            log.warn("Aucune livraion trouvée avec l'ID {}. Aucune conversion en DTO effectuée.", id);
            return null;
        }

        return LivraisonDto.fromEntity(findLivraison.get());
    }

    @Override
    public LivraisonDto updateStatut(Long id, String etat) {
        Objects.requireNonNull(id, "L'ID de la livraison ne peut pas être null");
        Optional<Livraison> livraisonOptional = livraisonRepository.findById(id);

        if (livraisonOptional.isPresent()) {
            Livraison livraison = livraisonOptional.get();

            if (livraison.getEtat() == LivraisonEtat.LIVRER) {
                log.warn("Livraison déjà livrée avec l'ID {}. Impossible de mettre à jour l'état.", id);
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
        } else {
            log.warn("Aucune livraison trouvée avec l'ID {}. Aucune conversion en DTO effectuée.", id);
            return null;
        }
    }

    public LivraisonDto affecterLivraison(Long livraisonId) {
        Utilisateur utilisateurEnCours = getUtilisateurEnCours();

        // Récupérer la livraison par ID
        Optional<Livraison> livraisonOptional = livraisonRepository.findById(livraisonId);

        if (livraisonOptional.isPresent()) {
            Livraison livraison = livraisonOptional.get();

            // Assurez-vous que la livraison n'est pas déjà affectée à un utilisateur
            if (livraison.getUtilisateur() == null) {
                // Affecter la livraison à l'utilisateur en cours
                livraison.setUtilisateur(utilisateurEnCours);
                livraisonRepository.save(livraison);

                return LivraisonDto.fromEntity(livraison);
            } else {
                log.warn("La livraison avec l'ID {} est déjà affectée à un utilisateur.", livraisonId);
            }
        } else {
            log.warn("Aucune livraison trouvée avec l'ID {}. Aucune affectation effectuée.", livraisonId);
        }

        return null;
    }

    public List<LivraisonDto> voirLivraisonsUtilisateurEnCours() {
        Utilisateur utilisateurEnCours = getUtilisateurEnCours();

        // Récupérer toutes les livraisons affectées à l'utilisateur en cours
        List<Livraison> livraisons = livraisonRepository.findByUtilisateur(utilisateurEnCours);

        // Convertir les livraisons en DTOs
        List<LivraisonDto> livraisonDtos = livraisons.stream()
                .map(LivraisonDto::fromEntity)
                .collect(Collectors.toList());

        return livraisonDtos;
    }



    public List<LivraisonDto> getLivraisonsByStatut(String statut) {
        try {
            LivraisonEtat etat = statut != null ? LivraisonEtat.valueOf(statut) : LivraisonEtat.EN_COURS;

            // Récupérer toutes les livraisons en fonction du statut
            List<Livraison> livraisons = livraisonRepository.findByEtat(etat);

            // Convertir les livraisons en DTOs
            List<LivraisonDto> livraisonDtos = livraisons.stream()
                    .map(LivraisonDto::fromEntity)
                    .collect(Collectors.toList());

            return livraisonDtos;
        } catch (IllegalArgumentException e) {
            log.error("Statut de livraison non valide : {}", statut);
            return Collections.emptyList();
        }
    }


    private Utilisateur getUtilisateurEnCours() {
        return (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
