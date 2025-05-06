package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniteRepository extends TenantAwareRepository<Unite,Long> {


}
