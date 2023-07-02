package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur,Long> {

    Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);

    List<CommandeClient> findAllByFournisseurId(Long id);
}
