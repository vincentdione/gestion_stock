package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends TenantUtilisateurAwareRepository<Utilisateur,Long> {

    List<Utilisateur> findByEntreprise(Entreprise entreprise);
    Optional<Utilisateur> findByUsername(String username);
//    @Query("select e from Utilisateur e where e.id = :id and e.entreprise.id = :idEntreprise")
//    Optional<Utilisateur> existsByIdAndEntreprise(@Param("id") Long id, @Param("idEntreprise") Long idEntreprise);
//
    Boolean existsByEmail(String email);
    boolean existsByEmailAndEntrepriseId(String email, Long entrepriseId);
    boolean existsByUsernameAndEntrepriseId(String username, Long entrepriseId);

}
