package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniteRepository extends TenantAwareRepository<Unite,Long> {

    // Méthode pour trouver une unité par nom et tenant
    Optional<Unite> findByNomAndIdEntreprise(String nom, Long idEntreprise);

}
