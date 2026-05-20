package com.ovd.gestionstock.specifications;

import com.ovd.gestionstock.criteria.CommandeFournisseurSearchCriteria;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.models.Fournisseur;
import com.ovd.gestionstock.models.ModePayement;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandeFournisseurSpecification {

    public static Specification<CommandeFournisseur> withCriteria(CommandeFournisseurSearchCriteria criteria, Long idEntreprise) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtre par entreprise (obligatoire)
            predicates.add(cb.equal(root.get("idEntreprise"), idEntreprise));

            // Joindre avec la table Fournisseur pour les recherches
            Join<CommandeFournisseur, Fournisseur> fournisseurJoin = root.join("fournisseur", JoinType.INNER);

            // Recherche par code commande
            if (StringUtils.hasText(criteria.getCode())) {
                predicates.add(cb.like(
                        cb.lower(root.get("code")),
                        "%" + criteria.getCode().toLowerCase() + "%"
                ));
            }

            // Recherche par nom fournisseur
            if (StringUtils.hasText(criteria.getNomFournisseur())) {
                predicates.add(cb.like(
                        cb.lower(fournisseurJoin.get("nom")),
                        "%" + criteria.getNomFournisseur().toLowerCase() + "%"
                ));
            }

            // Recherche par email fournisseur
            if (StringUtils.hasText(criteria.getEmailFournisseur())) {
                predicates.add(cb.like(
                        cb.lower(fournisseurJoin.get("email")),
                        "%" + criteria.getEmailFournisseur().toLowerCase() + "%"
                ));
            }

            // Recherche par numéro téléphone fournisseur
            if (StringUtils.hasText(criteria.getNumTelFournisseur())) {
                predicates.add(cb.like(
                        fournisseurJoin.get("numTel"),
                        "%" + criteria.getNumTelFournisseur() + "%"
                ));
            }

            // Filtre par état de commande
            if (StringUtils.hasText(criteria.getEtat())) {
                try {
                    CommandeEtat etat = CommandeEtat.valueOf(criteria.getEtat().toUpperCase());
                    predicates.add(cb.equal(root.get("etatCommande"), etat));
                } catch (IllegalArgumentException e) {
                    // Ignorer si l'état n'est pas valide
                }
            }

            // Filtre par mode de paiement (ID)
            if (criteria.getModePayementId() != null) {
                Join<CommandeFournisseur, ModePayement> modePayementJoin = root.join("modePayement", JoinType.INNER);
                predicates.add(cb.equal(modePayementJoin.get("id"), criteria.getModePayementId()));
            }

            // Filtre par code de mode de paiement
            if (StringUtils.hasText(criteria.getModePayementCode())) {
                Join<CommandeFournisseur, ModePayement> modePayementJoin = root.join("modePayement", JoinType.INNER);
                predicates.add(cb.like(
                        cb.lower(modePayementJoin.get("code")),
                        "%" + criteria.getModePayementCode().toLowerCase() + "%"
                ));
            }

            // Filtre par désignation de mode de paiement
            if (StringUtils.hasText(criteria.getModePayementDesignation())) {
                Join<CommandeFournisseur, ModePayement> modePayementJoin = root.join("modePayement", JoinType.INNER);
                predicates.add(cb.like(
                        cb.lower(modePayementJoin.get("designation")),
                        "%" + criteria.getModePayementDesignation().toLowerCase() + "%"
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
    public static Specification<CommandeFournisseur> hasCode(String code) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(code)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<CommandeFournisseur> hasNomFournisseur(String nomFournisseur) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(nomFournisseur)) {
                return cb.conjunction();
            }
            Join<CommandeFournisseur, Fournisseur> fournisseurJoin = root.join("fournisseur", JoinType.INNER);
            return cb.like(cb.lower(fournisseurJoin.get("nom")), "%" + nomFournisseur.toLowerCase() + "%");
        };
    }

    public static Specification<CommandeFournisseur> hasEtat(CommandeEtat etat) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (etat == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("etatCommande"), etat);
        };
    }

    public static Specification<CommandeFournisseur> hasModePayementId(Long modePayementId) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (modePayementId == null) {
                return cb.conjunction();
            }
            Join<CommandeFournisseur, ModePayement> modePayementJoin = root.join("modePayement", JoinType.INNER);
            return cb.equal(modePayementJoin.get("id"), modePayementId);
        };
    }

    public static Specification<CommandeFournisseur> belongsToEntreprise(Long idEntreprise) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.equal(root.get("idEntreprise"), idEntreprise);
    }

    public static Specification<CommandeFournisseur> betweenDates(Date from, Date to) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
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

    public static Specification<CommandeFournisseur> searchByText(String searchText) {
        return (Root<CommandeFournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(searchText)) {
                return cb.conjunction();
            }
            String searchTerm = searchText.toLowerCase();
            Join<CommandeFournisseur, Fournisseur> fournisseurJoin = root.join("fournisseur", JoinType.INNER);
            Join<CommandeFournisseur, ModePayement> modePayementJoin = root.join("modePayement", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("code")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(fournisseurJoin.get("nom")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(fournisseurJoin.get("prenom")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(fournisseurJoin.get("email")), "%" + searchTerm + "%"),
                    cb.like(fournisseurJoin.get("numTel"), "%" + searchTerm + "%"),
                    cb.like(cb.lower(modePayementJoin.get("code")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(modePayementJoin.get("designation")), "%" + searchTerm + "%")
            );
        };
    }
}