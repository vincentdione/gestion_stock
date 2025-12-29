package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.MvtStk;
import com.ovd.gestionstock.models.TypeMvtStk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkRepository extends TenantAwareRepository<MvtStk, Long> {

    @Query("select sum(m.quantite) from MvtStk m where m.article.id =:idArticle")
    BigDecimal stockReelArticle(@Param("idArticle") Long idArticle);

    @Query("select m.unite, sum(m.quantite) from MvtStk m where m.article.id =:idArticle group by m.unite")
    List<Object[]> stockReelArticleByUnite(@Param("idArticle") Long idArticle);

    @Query("SELECT COALESCE(SUM(m.quantite), 0) FROM MvtStk m WHERE m.typeMvtStk = :type AND m.article.id = :idArticle")
    BigDecimal sumQuantiteByTypeMvtStkAndArticleId(@Param("type") TypeMvtStk type, @Param("idArticle") Long idArticle);

    @Query("SELECT m.unite, COALESCE(SUM(m.quantite), 0) FROM MvtStk m WHERE m.typeMvtStk = :type AND m.article.id = :idArticle GROUP BY m.unite")
    List<Object[]> sumQuantiteByTypeMvtStkAndArticleIdGroupByUnite(@Param("type") TypeMvtStk type, @Param("idArticle") Long idArticle);

    List<MvtStk> findAllByArticleId(Long id);

    List<MvtStk> findAllByIdEntreprise(Long idEntreprise);

}
