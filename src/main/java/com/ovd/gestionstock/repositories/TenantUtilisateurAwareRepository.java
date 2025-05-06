package com.ovd.gestionstock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TenantUtilisateurAwareRepository<T, ID> extends JpaRepository<T, ID> {

    @Query("select e from #{#entityName} e where e.entreprise.id = ?#{@tenantContext.getCurrentTenant()}")
    @Override
    List<T> findAll();

    @Query("select e from #{#entityName} e where e.id = :id and e.entreprise.id = ?#{@tenantContext.getCurrentTenant()}")
    @Override
    Optional<T> findById(@Param("id") ID id);

    @Query("select count(e) > 0 from #{#entityName} e where e.id = :id and e.entreprise.id = ?#{@tenantContext.getCurrentTenant()}")
    boolean existsByIdAndTenant(@Param("id") ID id);
}