package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.models.LigneVente;
import com.ovd.gestionstock.models.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenteRepository extends TenantAwareRepository<Ventes,Long> {

    Optional<Ventes> findVentesByCode(String code);
}
