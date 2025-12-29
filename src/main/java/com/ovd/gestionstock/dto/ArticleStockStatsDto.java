package com.ovd.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStockStatsDto {
    private Long articleId;
    private String designation;
    private BigDecimal prixUnitaireHt;      // Prix d'achat HT
    private BigDecimal prixUnitaireTtc;     // Prix de vente TTC
    private BigDecimal stockRestant;
    private BigDecimal quantiteVendue;
    private BigDecimal montantTotal;       // Chiffre d'affaires (ventes)
    private BigDecimal montantTotalAchete; // Coût total des achats
    private BigDecimal benefice;           // Bénéfice

    // Méthode utilitaire pour calculer la marge
    public BigDecimal getMarge() {
        if (montantTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return benefice.divide(montantTotal, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}