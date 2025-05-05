package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur,Long> {

    Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);

    List<CommandeFournisseur> findAllByFournisseurId(Long id);

    List<CommandeFournisseur> findByFournisseurNomAndFournisseurEmailAndCode(String nom, String email, String codeCommande);

    List<CommandeFournisseur> findByFournisseurNomAndFournisseurEmail(String nom, String email);

    List<CommandeFournisseur> findByCode(String codeCommande);
}
