package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.ModePayement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ModePayementRepository extends TenantAwareRepository<ModePayement,Long> {
    Optional<ModePayement> findByCodeAndIdEntreprise(String code, Long idEntreprise);

}
