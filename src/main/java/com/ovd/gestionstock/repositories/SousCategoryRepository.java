package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.SousCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SousCategoryRepository extends TenantAwareRepository<SousCategory,Long> {
}
