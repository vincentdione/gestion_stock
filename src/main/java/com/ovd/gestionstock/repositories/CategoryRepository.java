package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends TenantAwareRepository<Category,Long> {
}
