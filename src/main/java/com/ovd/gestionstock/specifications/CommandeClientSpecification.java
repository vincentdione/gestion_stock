package com.ovd.gestionstock.specifications;

import com.ovd.gestionstock.criteria.CommandeClientSearchCriteria;
import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.models.ModePayement;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandeClientSpecification {

    public static Specification<CommandeClient> withCriteria(CommandeClientSearchCriteria criteria, Long idEntreprise) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtre par entreprise (obligatoire)
            predicates.add(cb.equal(root.get("idEntreprise"), idEntreprise));

            // Joindre avec la table Client pour les recherches
            Join<CommandeClient, Client> clientJoin = root.join("client", JoinType.INNER);

            // Recherche par code commande
            if (StringUtils.hasText(criteria.getCode())) {
                predicates.add(cb.like(
                        cb.lower(root.get("code")),
                        "%" + criteria.getCode().toLowerCase() + "%"
                ));
            }

            // Recherche par nom client
            if (StringUtils.hasText(criteria.getNomClient())) {
                predicates.add(cb.like(
                        cb.lower(clientJoin.get("nom")),
                        "%" + criteria.getNomClient().toLowerCase() + "%"
                ));
            }

            // Recherche par email client
            if (StringUtils.hasText(criteria.getEmailClient())) {
                predicates.add(cb.like(
                        cb.lower(clientJoin.get("email")),
                        "%" + criteria.getEmailClient().toLowerCase() + "%"
                ));
            }

            // Recherche par numéro téléphone client
            if (StringUtils.hasText(criteria.getNumTelClient())) {
                predicates.add(cb.like(
                        clientJoin.get("numTel"),
                        "%" + criteria.getNumTelClient() + "%"
                ));
            }

            // Filtre par état de commande
            if (StringUtils.hasText(criteria.getEtat())) {
                try {
                    CommandeEtat etat = CommandeEtat.valueOf(criteria.getEtat().toUpperCase());
                    predicates.add(cb.equal(root.get("etat"), etat));
                } catch (IllegalArgumentException e) {
                    // Ignorer si l'état n'est pas valide
                }
            }

            // Filtre par mode de paiement
            if (StringUtils.hasText(criteria.getModePayement())) {
                Join<CommandeClient, ModePayement> modePayementJoin = root.join("modePayement", JoinType.INNER);
                predicates.add(cb.like(
                        cb.lower(modePayementJoin.get("code")),
                        "%" + criteria.getModePayement().toLowerCase() + "%"
                ));
            }

            // Filtre par date (from)
            if (criteria.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("dateCommande"),
                        criteria.getDateFrom()
                ));
            }

            // Filtre par date (to)
            if (criteria.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("dateCommande"),
                        criteria.getDateTo()
                ));
            }

            // Tri par date de commande décroissante par défaut
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("dateCommande")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Spécifications réutilisables
    public static Specification<CommandeClient> hasCode(String code) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(code)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<CommandeClient> hasNomClient(String nomClient) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(nomClient)) {
                return cb.conjunction();
            }
            Join<CommandeClient, Client> clientJoin = root.join("client", JoinType.INNER);
            return cb.like(cb.lower(clientJoin.get("nom")), "%" + nomClient.toLowerCase() + "%");
        };
    }

    public static Specification<CommandeClient> hasEtat(CommandeEtat etat) {
        return (root, query, cb) -> {
            if (etat == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("etat"), etat);
        };
    }

    public static Specification<CommandeClient> belongsToEntreprise(Long idEntreprise) {
        return (root, query, cb) -> cb.equal(root.get("idEntreprise"), idEntreprise);
    }

    public static Specification<CommandeClient> betweenDates(Date from, Date to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }
            if (from != null && to != null) {
                return cb.between(root.get("dateCommande"), from, to);
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("dateCommande"), from);
            }
            return cb.lessThanOrEqualTo(root.get("dateCommande"), to);
        };
    }

    public static Specification<CommandeClient> searchByText(String searchText) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(searchText)) {
                return cb.conjunction();
            }
            String searchTerm = searchText.toLowerCase();
            Join<CommandeClient, Client> clientJoin = root.join("client", JoinType.INNER);
            return cb.or(
                    cb.like(cb.lower(root.get("code")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(clientJoin.get("nom")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(clientJoin.get("prenom")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(clientJoin.get("email")), "%" + searchTerm + "%"),
                    cb.like(clientJoin.get("numTel"), "%" + searchTerm + "%")
            );
        };
    }
}