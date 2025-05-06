package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Livraison;
import com.ovd.gestionstock.models.LivraisonEtat;
import com.ovd.gestionstock.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivraisonRepository extends TenantAwareRepository<Livraison,Long> {

    List<Livraison> findByUtilisateur(Utilisateur utilisateur);
    List<Livraison> findByEtat(LivraisonEtat etat);
}
