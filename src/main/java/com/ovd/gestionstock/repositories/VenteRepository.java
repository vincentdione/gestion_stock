package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Ventes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VenteRepository extends TenantAwareRepository<Ventes,Long> {

    Optional<Ventes> findVentesByCode(String code);

    @Query("SELECT v FROM Ventes v WHERE v.idEntreprise = :idEntreprise ORDER BY v.dateVente DESC")
    List<Ventes> findTop10ByOrderByDateVenteDesc(@Param("idEntreprise") Long idEntreprise);

    // Trouver les ventes par période
    List<Ventes> findByDateVenteBetweenAndIdEntreprise(Date dateDebut, Date dateFin, Long idEntreprise);

    // Trouver les ventes d'aujourd'hui
    @Query("SELECT v FROM Ventes v WHERE DATE(v.dateVente) = CURRENT_DATE AND v.idEntreprise = :idEntreprise")
    List<Ventes> findVentesDuJour(@Param("idEntreprise") Long idEntreprise);

    // CA par jour - CORRIGÉE: Calculer le montantTotal depuis les lignes de vente
    @Query("SELECT DATE(v.dateVente) as jour, " +
            "SUM(CASE WHEN lv.prixUnitaire IS NOT NULL AND lv.quantite IS NOT NULL " +
            "THEN lv.prixUnitaire * lv.quantite ELSE 0 END) as chiffreAffaire " +
            "FROM Ventes v " +
            "LEFT JOIN v.ligneVentes lv " +
            "WHERE v.dateVente BETWEEN :debut AND :fin " +
            "AND v.idEntreprise = :idEntreprise " +
            "GROUP BY DATE(v.dateVente) " +
            "ORDER BY jour DESC")
    List<Object[]> findChiffreAffaireParJour(@Param("debut") Date debut,
                                             @Param("fin") Date fin,
                                             @Param("idEntreprise") Long idEntreprise);

    // CA par mois - CORRIGÉE: Utiliser la fonction appropriée pour MySQL
    @Query("SELECT FUNCTION('DATE_FORMAT', v.dateVente, '%Y-%m'), " +
            "FUNCTION('DATE_FORMAT', v.dateVente, '%M %Y'), " +
            "SUM(CASE WHEN lv.prixUnitaire IS NOT NULL AND lv.quantite IS NOT NULL " +
            "THEN lv.prixUnitaire * lv.quantite ELSE 0 END) " +
            "FROM Ventes v " +
            "LEFT JOIN v.ligneVentes lv " +
            "WHERE v.dateVente BETWEEN :debut AND :fin " +
            "AND v.idEntreprise = :idEntreprise " +
            "GROUP BY FUNCTION('DATE_FORMAT', v.dateVente, '%Y-%m'), " +
            "FUNCTION('DATE_FORMAT', v.dateVente, '%M %Y') " +
            "ORDER BY FUNCTION('DATE_FORMAT', v.dateVente, '%Y-%m') DESC")
    List<Object[]> findChiffreAffaireParMois(@Param("debut") Date debut,
                                             @Param("fin") Date fin,
                                             @Param("idEntreprise") Long idEntreprise);

    // Méthode utilitaire pour calculer le total des ventes
    @Query("SELECT SUM(CASE WHEN lv.prixUnitaire IS NOT NULL AND lv.quantite IS NOT NULL " +
            "THEN lv.prixUnitaire * lv.quantite ELSE 0 END) " +
            "FROM Ventes v " +
            "LEFT JOIN v.ligneVentes lv " +
            "WHERE v.idEntreprise = :idEntreprise")
    BigDecimal getTotalChiffreAffaire(@Param("idEntreprise") Long idEntreprise);

}
