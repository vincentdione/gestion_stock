package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Category;
import com.ovd.gestionstock.models.SousCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends TenantAwareRepository<Category,Long> {
    Optional<Category> findByCodeAndIdEntreprise(String code, Long idEntreprise);
}
