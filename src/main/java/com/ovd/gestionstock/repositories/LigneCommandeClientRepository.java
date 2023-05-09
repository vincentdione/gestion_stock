package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.LigneCommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient,Long> {

    public List<LigneCommandeClient> findAllByCommandeClientId(Long id);
    public List<LigneCommandeClient> findAllByArticleId(Long id);
}
