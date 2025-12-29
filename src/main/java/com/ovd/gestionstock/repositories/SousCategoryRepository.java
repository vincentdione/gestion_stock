package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.SousCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SousCategoryRepository extends TenantAwareRepository<SousCategory,Long> {
    Optional<SousCategory> findByCodeAndIdEntreprise(String code, Long idEntreprise);

}
