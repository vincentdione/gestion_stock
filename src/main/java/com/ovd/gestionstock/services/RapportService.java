package com.ovd.gestionstock.services;

import com.ovd.gestionstock.criteria.RapportSearchCriteria;
import com.ovd.gestionstock.dto.RapportDto;

public interface RapportService {

    /**
     * Générer un rapport complet
     */
    RapportDto genererRapport(RapportSearchCriteria criteria);

    /**
     * Rapport rapide pour hier
     */
    RapportDto rapportHier();

    /**
     * Rapport rapide pour la semaine
     */
    RapportDto rapportSemaine();

    /**
     * Rapport rapide pour le mois en cours
     */
    RapportDto rapportMoisEnCours();

    /**
     * Rapport rapide pour l'année en cours
     */
    RapportDto rapportAnneeEnCours();

    /**
     * Top 10 des articles les plus vendus sur une période
     */
    RapportDto topArticles(RapportSearchCriteria criteria);

    /**
     * Chiffre d'affaire par jour/mois/année
     */
    RapportDto chiffreAffaireParPeriode(RapportSearchCriteria criteria);
}