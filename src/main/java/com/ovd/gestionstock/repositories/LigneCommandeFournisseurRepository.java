package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.LigneCommandeClient;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeFournisseurRepository extends TenantAwareRepository<LigneCommandeFournisseur,Long> {

    public List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Long id);
    public List<LigneCommandeFournisseur> findAllByArticleId(Long id);
}
