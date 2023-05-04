package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenteRepository extends JpaRepository<Ventes,Long> {

}
