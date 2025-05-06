package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FournisseurRepository extends TenantAwareRepository<Fournisseur,Long> {
    List<Fournisseur> findAllByIdEntreprise(Long idEntreprise);

}
