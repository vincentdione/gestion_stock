package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.ConditionAV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConditionAVRepository extends TenantAwareRepository<ConditionAV,Long> {
    List<ConditionAV> findByArticleId(Long articleId);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ConditionAV c WHERE c.article = :article " +
            "AND c.unite.nom = :uniteNom AND c.idEntreprise = :idEntreprise")
    boolean existsByArticleAndUniteNom(@Param("article") Article article,
                                       @Param("uniteNom") String uniteNom,
                                       @Param("idEntreprise") Long idEntreprise);
}
