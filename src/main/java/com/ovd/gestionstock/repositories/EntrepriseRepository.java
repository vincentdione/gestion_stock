package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise,Long> {
}
