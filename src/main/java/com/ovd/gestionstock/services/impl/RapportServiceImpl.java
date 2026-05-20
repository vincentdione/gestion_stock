package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.criteria.RapportSearchCriteria;
import com.ovd.gestionstock.dto.*;
import com.ovd.gestionstock.models.*;
import com.ovd.gestionstock.repositories.*;
import com.ovd.gestionstock.services.RapportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RapportServiceImpl implements RapportService {

    private final VenteRepository venteRepository;
    private final CommandeClientRepository commandeClientRepository;
    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final ArticleRepository articleRepository;
    private final TenantContext tenantContext;

    @Override
    public RapportDto genererRapport(RapportSearchCriteria criteria) {
        log.info("Génération du rapport pour la période: {}", criteria.getPeriode());

        criteria.calculerDates();
        Long idEntreprise = tenantContext.getCurrentTenant();

        log.info("Entreprise ID: {}", idEntreprise);
        log.info("Date début: {}, Date fin: {}", criteria.getDateDebut(), criteria.getDateFin());

        if (idEntreprise == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        RapportDto.RapportDtoBuilder builder = RapportDto.builder()
                .dateDebut(criteria.getDateDebut())
                .dateFin(criteria.getDateFin())
                .typeRapport(criteria.getPeriode().name());

        // 1. Récupérer les ventes dans la période avec les lignes chargées
        List<Ventes> ventes = getVentesParPeriode(criteria, idEntreprise);

        // Forcer le calcul du montant total pour chaque vente
        ventes.forEach(Ventes::calculerMontantTotal);

        // Log pour déboguer
        ventes.forEach(vente -> {
            log.debug("Vente ID: {}, Date: {}, Montant: {}, Lignes: {}",
                    vente.getId(), vente.getDateVente(),
                    vente.getMontantTotal(),
                    vente.getLigneVentes() != null ? vente.getLigneVentes().size() : 0);
        });

        log.info("Nombre total de ventes: {}", ventes.size());
        builder.nombreVentes((long) ventes.size());

        // 2. Calculer le chiffre d'affaire
        BigDecimal chiffreAffaire = calculerChiffreAffaire(ventes);
        log.info("Chiffre d'affaire calculé: {}", chiffreAffaire);
        builder.chiffreAffaireTotal(chiffreAffaire);

        // 3. Calculer le montant moyen par vente
        BigDecimal montantMoyen = ventes.isEmpty() ? BigDecimal.ZERO :
                chiffreAffaire.divide(BigDecimal.valueOf(ventes.size()), 2, RoundingMode.HALF_UP);
        builder.montantMoyenVente(montantMoyen);

        // 4. Récupérer les commandes clients
        List<CommandeClient> commandesClients = getCommandesClientsParPeriode(criteria, idEntreprise);
        log.info("Nombre de commandes clients: {}", commandesClients.size());
        builder.nombreCommandesClients((long) commandesClients.size());

        // 5. Récupérer les commandes fournisseurs
        List<CommandeFournisseur> commandesFournisseurs = getCommandesFournisseursParPeriode(criteria, idEntreprise);
        log.info("Nombre de commandes fournisseurs: {}", commandesFournisseurs.size());
        builder.nombreCommandesFournisseurs((long) commandesFournisseurs.size());

        // 6. Calculer l'évolution journalière
        if (criteria.getAvecDetails() != null && criteria.getAvecDetails()) {
            List<EvolutionJournaliereDto> evolutionJournaliere = calculerEvolutionJournaliere(ventes, commandesClients);
            List<EvolutionMensuelleDto> evolutionMensuelle = calculerEvolutionMensuelle(ventes, commandesClients);
            log.info("Évolution journalière: {} jours", evolutionJournaliere.size());
            log.info("Évolution mensuelle: {} mois", evolutionMensuelle.size());
            builder.evolutionJournaliere(evolutionJournaliere);
            builder.evolutionMensuelle(evolutionMensuelle);
        }

        // 7. Top des articles
        int limit = criteria.getLimitArticles() != null ? criteria.getLimitArticles() : 10;
        List<ArticleVenteDto> articlesPlusVendus = getArticlesPlusVendus(ventes, limit);
        List<ArticleQuantiteDto> articlesQuantite = getArticlesParQuantite(ventes, limit);
        log.info("Articles plus vendus: {}", articlesPlusVendus.size());
        log.info("Articles par quantité: {}", articlesQuantite.size());
        builder.articlesPlusVendus(articlesPlusVendus);
        builder.articlesQuantite(articlesQuantite);

        // 8. Statut des commandes
        builder.statutCommandesClients(calculerStatutCommandesClients(commandesClients));
        builder.statutCommandesFournisseurs(calculerStatutCommandesFournisseurs(commandesFournisseurs));

        return builder.build();
    }

    @Override
    public RapportDto rapportHier() {
        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(PeriodeRapport.HIER)
                .avecDetails(true)
                .limitArticles(5)
                .build();
        criteria.calculerDates();

        return genererRapport(criteria);
    }

    @Override
    public RapportDto rapportSemaine() {
        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(PeriodeRapport.SEMAINE)
                .avecDetails(true)
                .limitArticles(10)
                .build();
        criteria.calculerDates();

        return genererRapport(criteria);
    }

    @Override
    public RapportDto rapportMoisEnCours() {
        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(PeriodeRapport.MOIS_EN_COURS)
                .avecDetails(true)
                .limitArticles(15)
                .build();
        criteria.calculerDates();

        return genererRapport(criteria);
    }

    @Override
    public RapportDto rapportAnneeEnCours() {
        RapportSearchCriteria criteria = RapportSearchCriteria.builder()
                .periode(PeriodeRapport.ANNEE_EN_COURS)
                .avecDetails(true)
                .limitArticles(20)
                .build();
        criteria.calculerDates();

        return genererRapport(criteria);
    }

    @Override
    public RapportDto topArticles(RapportSearchCriteria criteria) {
        Long idEntreprise = tenantContext.getCurrentTenant();
        int limit = criteria.getLimitArticles() != null ? criteria.getLimitArticles() : 10;

        List<Ventes> ventes = getVentesParPeriode(criteria, idEntreprise);
        List<ArticleVenteDto> articlesPlusVendus = getArticlesPlusVendus(ventes, limit);

        return RapportDto.builder()
                .dateDebut(criteria.getDateDebut())
                .dateFin(criteria.getDateFin())
                .articlesPlusVendus(articlesPlusVendus)
                .build();
    }

    @Override
    public RapportDto chiffreAffaireParPeriode(RapportSearchCriteria criteria) {
        Long idEntreprise = tenantContext.getCurrentTenant();

        List<Ventes> ventes = getVentesParPeriode(criteria, idEntreprise);
        List<CommandeClient> commandesClients = getCommandesClientsParPeriode(criteria, idEntreprise);

        List<EvolutionJournaliereDto> evolutionJournaliere = calculerEvolutionJournaliere(ventes, commandesClients);
        List<EvolutionMensuelleDto> evolutionMensuelle = calculerEvolutionMensuelle(ventes, commandesClients);

        BigDecimal chiffreAffaireTotal = evolutionJournaliere.stream()
                .map(EvolutionJournaliereDto::getChiffreAffaire)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RapportDto.builder()
                .dateDebut(criteria.getDateDebut())
                .dateFin(criteria.getDateFin())
                .chiffreAffaireTotal(chiffreAffaireTotal)
                .evolutionJournaliere(evolutionJournaliere)
                .evolutionMensuelle(evolutionMensuelle)
                .build();
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private List<Ventes> getVentesParPeriode(RapportSearchCriteria criteria, Long idEntreprise) {
        if (criteria.getDateDebut() == null || criteria.getDateFin() == null) {
            return List.of();
        }

        List<Ventes> ventes = venteRepository.findByDateVenteBetweenAndIdEntreprise(
                criteria.getDateDebut(),
                criteria.getDateFin(),
                idEntreprise
        );

        // S'assurer que les lignes sont chargées pour chaque vente
        for (Ventes vente : ventes) {
            if (vente.getLigneVentes() != null) {
                // Initialiser la collection (force le chargement si nécessaire)
                vente.getLigneVentes().size();
            }
        }

        return ventes;
    }

    private List<CommandeClient> getCommandesClientsParPeriode(RapportSearchCriteria criteria, Long idEntreprise) {
        if (criteria.getDateDebut() == null || criteria.getDateFin() == null) {
            return List.of();
        }

        return commandeClientRepository.findByDateCommandeBetweenAndIdEntreprise(
                criteria.getDateDebut(),
                criteria.getDateFin(),
                idEntreprise
        );
    }

    private List<CommandeFournisseur> getCommandesFournisseursParPeriode(RapportSearchCriteria criteria, Long idEntreprise) {
        if (criteria.getDateDebut() == null || criteria.getDateFin() == null) {
            return List.of();
        }

        Instant dateDebutInstant = convertDateToInstant(criteria.getDateDebut());
        Instant dateFinInstant = convertDateToInstant(criteria.getDateFin());

        return commandeFournisseurRepository.findByDateCommandeBetweenAndIdEntreprise(
                dateDebutInstant,
                dateFinInstant,
                idEntreprise
        );
    }

    private Instant convertDateToInstant(Date date) {
        if (date == null) {
            return null;
        }

        if (date instanceof java.sql.Date) {
            return new Date(date.getTime()).toInstant();
        }

        return date.toInstant();
    }

    private BigDecimal calculerChiffreAffaire(List<Ventes> ventes) {
        BigDecimal total = BigDecimal.ZERO;

        for (Ventes vente : ventes) {
            BigDecimal montantVente = vente.getMontantTotal();
            if (montantVente != null) {
                total = total.add(montantVente);
                log.debug("Ajout montant vente {}: {}", vente.getId(), montantVente);
            } else {
                log.warn("Montant null pour vente ID: {}", vente.getId());
            }
        }

        log.info("Total CA calculé: {}", total);
        return total;
    }

    private List<EvolutionJournaliereDto> calculerEvolutionJournaliere(List<Ventes> ventes, List<CommandeClient> commandesClients) {
        if (ventes.isEmpty()) {
            return List.of();
        }

        // Grouper les ventes par jour
        Map<LocalDate, List<Ventes>> ventesParJour = ventes.stream()
                .collect(Collectors.groupingBy(vente ->
                        vente.getDateVente().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                ));

        // Grouper les commandes par jour
        Map<LocalDate, List<CommandeClient>> commandesParJour = commandesClients.stream()
                .collect(Collectors.groupingBy(commande ->
                        commande.getDateCommande().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                ));

        return ventesParJour.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Ventes> ventesDuJour = entry.getValue();

                    BigDecimal chiffreAffaire = ventesDuJour.stream()
                            .map(Ventes::getMontantTotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int nombreVentes = ventesDuJour.size();
                    int nombreCommandes = commandesParJour.getOrDefault(date, List.of()).size();

                    return EvolutionJournaliereDto.builder()
                            .date(date)
                            .chiffreAffaire(chiffreAffaire)
                            .nombreVentes(nombreVentes)
                            .nombreCommandes(nombreCommandes)
                            .build();
                })
                .sorted(Comparator.comparing(EvolutionJournaliereDto::getDate).reversed())
                .collect(Collectors.toList());
    }

    private List<EvolutionMensuelleDto> calculerEvolutionMensuelle(List<Ventes> ventes, List<CommandeClient> commandesClients) {
        if (ventes.isEmpty()) {
            return List.of();
        }

        // Grouper les ventes par mois
        Map<YearMonth, List<Ventes>> ventesParMois = ventes.stream()
                .collect(Collectors.groupingBy(vente ->
                        YearMonth.from(vente.getDateVente().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                ));

        // Grouper les commandes par mois
        Map<YearMonth, List<CommandeClient>> commandesParMois = commandesClients.stream()
                .collect(Collectors.groupingBy(commande ->
                        YearMonth.from(commande.getDateCommande().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                ));

        return ventesParMois.entrySet().stream()
                .map(entry -> {
                    YearMonth mois = entry.getKey();
                    List<Ventes> ventesDuMois = entry.getValue();

                    BigDecimal chiffreAffaire = ventesDuMois.stream()
                            .map(Ventes::getMontantTotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int nombreVentes = ventesDuMois.size();
                    int nombreCommandes = commandesParMois.getOrDefault(mois, List.of()).size();

                    // Formatter le libellé du mois
                    String moisEnFrancais = getMoisEnFrancais(mois.getMonthValue());
                    String libelleMois = moisEnFrancais + " " + mois.getYear();

                    return EvolutionMensuelleDto.builder()
                            .mois(mois.toString())
                            .libelleMois(libelleMois)
                            .chiffreAffaire(chiffreAffaire)
                            .nombreVentes(nombreVentes)
                            .nombreCommandes(nombreCommandes)
                            .build();
                })
                .sorted(Comparator.comparing(EvolutionMensuelleDto::getMois).reversed())
                .collect(Collectors.toList());
    }

    private String getMoisEnFrancais(int mois) {
        String[] moisFrancais = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        return moisFrancais[mois - 1];
    }

    private List<ArticleVenteDto> getArticlesPlusVendus(List<Ventes> ventes, int limit) {
        if (ventes.isEmpty()) {
            return List.of();
        }

        // Extraire toutes les lignes de vente
        List<LigneVente> toutesLignesVentes = new ArrayList<>();
        for (Ventes vente : ventes) {
            // Vérifier si la collection n'est pas nulle
            if (vente.getLigneVentes() != null) {
                toutesLignesVentes.addAll(vente.getLigneVentes());
            }
        }

        // Grouper par article et calculer les montants
        Map<Article, ArticleStats> statsParArticle = toutesLignesVentes.stream()
                .collect(Collectors.groupingBy(
                        LigneVente::getArticle,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lignes -> {
                                    BigDecimal montantTotal = lignes.stream()
                                            .map(ligne -> {
                                                if (ligne.getPrixUnitaire() != null && ligne.getQuantite() != null) {
                                                    return ligne.getPrixUnitaire().multiply(new BigDecimal(String.valueOf(ligne.getQuantite())));
                                                }
                                                return BigDecimal.ZERO;
                                            })
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    BigDecimal quantiteTotale = lignes.stream()
                                            .map(ligne -> {
                                                if (ligne.getQuantite() != null) {
                                                    return new BigDecimal(String.valueOf(ligne.getQuantite()));
                                                }
                                                return BigDecimal.ZERO;
                                            })
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    return new ArticleStats(montantTotal, quantiteTotale);
                                }
                        )
                ));

        // Calculer le CA total pour les pourcentages
        BigDecimal totalCA = statsParArticle.values().stream()
                .map(ArticleStats::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Créer les DTOs et trier par montant
        return statsParArticle.entrySet().stream()
                .map(entry -> {
                    Article article = entry.getKey();
                    ArticleStats stats = entry.getValue();

                    BigDecimal pourcentageCA = BigDecimal.ZERO;
                    if (totalCA.compareTo(BigDecimal.ZERO) > 0) {
                        pourcentageCA = stats.getMontantTotal()
                                .divide(totalCA, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100"));
                    }

                    return ArticleVenteDto.builder()
                            .id(article.getId())
                            .code(article.getCodeArticle())
                            .nom(article.getDesignation())
                            .montantTotalVendu(stats.getMontantTotal())
                            .quantiteVendue(stats.getQuantiteTotale())
                            .pourcentageCA(pourcentageCA)
                            .build();
                })
                .sorted(Comparator.comparing(ArticleVenteDto::getMontantTotalVendu).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<ArticleQuantiteDto> getArticlesParQuantite(List<Ventes> ventes, int limit) {
        if (ventes.isEmpty()) {
            return List.of();
        }

        // Extraire toutes les lignes de vente
        List<LigneVente> toutesLignesVentes = new ArrayList<>();
        for (Ventes vente : ventes) {
            if (vente.getLigneVentes() != null) {
                toutesLignesVentes.addAll(vente.getLigneVentes());
            }
        }

        // Grouper par article et calculer les quantités
        Map<Article, BigDecimal> quantitesParArticle = toutesLignesVentes.stream()
                .collect(Collectors.groupingBy(
                        LigneVente::getArticle,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                ligne -> ligne.getQuantite() != null ? new BigDecimal(String.valueOf(ligne.getQuantite())) : BigDecimal.ZERO,
                                BigDecimal::add
                        )
                ));

        // Créer les DTOs et trier par quantité
        return quantitesParArticle.entrySet().stream()
                .map(entry -> {
                    Article article = entry.getKey();
                    BigDecimal quantite = entry.getValue();

//                    List<String> unitesCodes = article.getUnites() != null ?
//                            article.getUnites().stream()
//                                    .map(Unite::getNom)
//                                    .collect(Collectors.toList())
//                            : new ArrayList<>();

                    return ArticleQuantiteDto.builder()
                            .id(article.getId())
                            .code(article.getCodeArticle())
                            .nom(article.getDesignation())
                            .unite(article.getUnites() != null && !article.getUnites().isEmpty() ?
                                    article.getUnites().get(0).getNom() : "")
                            .quantiteVendue(quantite)
                            .build();
                })
                .sorted(Comparator.comparing(ArticleQuantiteDto::getQuantiteVendue).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private StatutCommandesDto calculerStatutCommandesClients(List<CommandeClient> commandes) {
        Map<CommandeEtat, Long> comptage = commandes.stream()
                .collect(Collectors.groupingBy(
                        CommandeClient::getEtat,
                        Collectors.counting()
                ));

        return StatutCommandesDto.builder()
                .total((long) commandes.size())
                .enPreparation(comptage.getOrDefault(CommandeEtat.EN_PREPARATION, 0L))
                .validee(comptage.getOrDefault(CommandeEtat.VALIDEE, 0L))
                .livree(comptage.getOrDefault(CommandeEtat.LIVREE, 0L))
                .annulee(comptage.getOrDefault(CommandeEtat.ANNULEE, 0L))
                .build();
    }

    private StatutCommandesDto calculerStatutCommandesFournisseurs(List<CommandeFournisseur> commandes) {
        Map<CommandeEtat, Long> comptage = commandes.stream()
                .collect(Collectors.groupingBy(
                        CommandeFournisseur::getEtatCommande,
                        Collectors.counting()
                ));

        return StatutCommandesDto.builder()
                .total((long) commandes.size())
                .enPreparation(comptage.getOrDefault(CommandeEtat.EN_PREPARATION, 0L))
                .validee(comptage.getOrDefault(CommandeEtat.VALIDEE, 0L))
                .livree(comptage.getOrDefault(CommandeEtat.LIVREE, 0L))
                .annulee(comptage.getOrDefault(CommandeEtat.ANNULEE, 0L))
                .build();
    }

    // Classe interne pour les statistiques d'article
    private static class ArticleStats {
        private final BigDecimal montantTotal;
        private final BigDecimal quantiteTotale;

        public ArticleStats(BigDecimal montantTotal, BigDecimal quantiteTotale) {
            this.montantTotal = montantTotal;
            this.quantiteTotale = quantiteTotale;
        }

        public BigDecimal getMontantTotal() {
            return montantTotal;
        }

        public BigDecimal getQuantiteTotale() {
            return quantiteTotale;
        }
    }
}