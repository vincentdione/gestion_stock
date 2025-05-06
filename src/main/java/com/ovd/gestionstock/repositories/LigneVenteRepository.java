package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneVenteRepository extends TenantAwareRepository<LigneVente,Long> {

    List<LigneVente> findAllByArticleId(Long idArticle);
    List<LigneVente> findAllByVenteId(Long idVente);
    List<LigneVente> findByVenteCode(String codeVente);

}
