package com.ovd.gestionstock.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RapportDto {

    // Période du rapport
    private Date dateDebut;
    private Date dateFin;
    private String typeRapport; // JOUR, SEMAINE, MOIS, ANNEE

    // Statistiques générales
    private BigDecimal chiffreAffaireTotal;
    private Long nombreVentes;
    private Long nombreCommandesClients;
    private Long nombreCommandesFournisseurs;
    private BigDecimal montantMoyenVente;

    // Évolution temporelle
    private List<EvolutionJournaliereDto> evolutionJournaliere;
    private List<EvolutionMensuelleDto> evolutionMensuelle;

    // Top articles
    private List<ArticleVenteDto> articlesPlusVendus;
    private List<ArticleQuantiteDto> articlesQuantite;

    // Commandes par statut
    private StatutCommandesDto statutCommandesClients;
    private StatutCommandesDto statutCommandesFournisseurs;

}