package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeFournisseur;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurRepository extends TenantAwareRepository<CommandeFournisseur,Long> {

    Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);

    List<CommandeFournisseur> findAllByFournisseurId(Long id);

    List<CommandeFournisseur> findByFournisseurNomAndFournisseurEmailAndCode(String nom, String email, String codeCommande);

    List<CommandeFournisseur> findByFournisseurNomAndFournisseurEmail(String nom, String email);

    List<CommandeFournisseur> findByCode(String codeCommande);

    List<CommandeFournisseur> findByDateCommandeBetweenAndIdEntreprise(Instant dateDebut,Instant dateFin, Long idEntreprise);
}
