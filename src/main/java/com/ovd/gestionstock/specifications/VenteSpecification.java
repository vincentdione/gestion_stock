package com.ovd.gestionstock.specifications;


import com.ovd.gestionstock.criteria.VenteSearchCriteria;
import com.ovd.gestionstock.models.Ventes;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VenteSpecification {

    public static Specification<Ventes> withCriteria(VenteSearchCriteria criteria, Long idEntreprise) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtre par entreprise (obligatoire)
            predicates.add(cb.equal(root.get("idEntreprise"), idEntreprise));

            // Recherche par nom client (insensible à la casse, recherche partielle)
            if (StringUtils.hasText(criteria.getNomClient())) {
                predicates.add(cb.like(
                        cb.lower(root.get("nomClient")),
                        "%" + criteria.getNomClient().toLowerCase() + "%"
                ));
            }

            // Recherche par prénom client
            if (StringUtils.hasText(criteria.getPrenomClient())) {
                predicates.add(cb.like(
                        cb.lower(root.get("prenomClient")),
                        "%" + criteria.getPrenomClient().toLowerCase() + "%"
                ));
            }

            // Recherche par code vente
            if (StringUtils.hasText(criteria.getCodeVente())) {
                predicates.add(cb.like(
                        cb.lower(root.get("code")),
                        "%" + criteria.getCodeVente().toLowerCase() + "%"
                ));
            }

            // Recherche par numéro client
            if (StringUtils.hasText(criteria.getNumeroClient())) {
                predicates.add(cb.like(
                        root.get("numero"),
                        "%" + criteria.getNumeroClient() + "%"
                ));
            }

            // Recherche par adresse
            if (StringUtils.hasText(criteria.getAdresse())) {
                predicates.add(cb.like(
                        cb.lower(root.get("adresse")),
                        "%" + criteria.getAdresse().toLowerCase() + "%"
                ));
            }

            // Filtre par date (from)
            if (criteria.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("dateVente"),
                        criteria.getDateFrom()
                ));
            }

            // Filtre par date (to)
            if (criteria.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("dateVente"),
                        criteria.getDateTo()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Spécifications additionnelles réutilisables
    public static Specification<Ventes> hasNomClient(String nomClient) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(nomClient)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nomClient")), "%" + nomClient.toLowerCase() + "%");
        };
    }

    public static Specification<Ventes> hasCodeVente(String code) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(code)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<Ventes> belongsToEntreprise(Long idEntreprise) {
        return (root, query, cb) -> cb.equal(root.get("idEntreprise"), idEntreprise);
    }

    public static Specification<Ventes> betweenDates(Date from, Date to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }
            if (from != null && to != null) {
                return cb.between(root.get("dateVente"), from, to);
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("dateVente"), from);
            }
            return cb.lessThanOrEqualTo(root.get("dateVente"), to);
        };
    }
}