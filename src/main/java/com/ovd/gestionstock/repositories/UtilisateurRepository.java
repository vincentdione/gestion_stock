package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    Optional<Utilisateur> findByUsername(String username);

}
