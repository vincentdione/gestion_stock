package com.ovd.gestionstock.specifications;


import com.ovd.gestionstock.criteria.ClientSearchCriteria;
import com.ovd.gestionstock.models.Client;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification {

    public static Specification<Client> withCriteria(ClientSearchCriteria criteria, Long idEntreprise) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtre par entreprise (obligatoire)
            predicates.add(cb.equal(root.get("idEntreprise"), idEntreprise));

            // Recherche par nom (insensible à la casse, recherche partielle)
            if (StringUtils.hasText(criteria.getNom())) {
                predicates.add(cb.like(
                        cb.lower(root.get("nom")),
                        "%" + criteria.getNom().toLowerCase() + "%"
                ));
            }

            // Recherche par prénom
            if (StringUtils.hasText(criteria.getPrenom())) {
                predicates.add(cb.like(
                        cb.lower(root.get("prenom")),
                        "%" + criteria.getPrenom().toLowerCase() + "%"
                ));
            }

            // Recherche par email
            if (StringUtils.hasText(criteria.getEmail())) {
                predicates.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + criteria.getEmail().toLowerCase() + "%"
                ));
            }

            // Recherche par numéro de téléphone
            if (StringUtils.hasText(criteria.getNumTel())) {
                predicates.add(cb.like(
                        root.get("numTel"),
                        "%" + criteria.getNumTel() + "%"
                ));
            }

            // Recherche par adresse (recherche dans rue, ville, code postal)
            if (StringUtils.hasText(criteria.getAdresse())) {
                String searchTerm = criteria.getAdresse().toLowerCase();
                Predicate adressePredicate = cb.or(
                        cb.like(cb.lower(root.get("adresse").get("rue")), "%" + searchTerm + "%"),
                        cb.like(cb.lower(root.get("adresse").get("ville")), "%" + searchTerm + "%"),
                        cb.like(root.get("adresse").get("codePostal"), "%" + criteria.getAdresse() + "%")
                );
                predicates.add(adressePredicate);
            }

            // Recherche spécifique par ville
            if (StringUtils.hasText(criteria.getVille())) {
                predicates.add(cb.like(
                        cb.lower(root.get("adresse").get("ville")),
                        "%" + criteria.getVille().toLowerCase() + "%"
                ));
            }

            // Recherche spécifique par code postal
            if (StringUtils.hasText(criteria.getCodePostal())) {
                predicates.add(cb.like(
                        root.get("adresse").get("codePostal"),
                        "%" + criteria.getCodePostal() + "%"
                ));
            }

            // Tri par nom et prénom par défaut
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.asc(root.get("nom")), cb.asc(root.get("prenom")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Spécifications réutilisables
    public static Specification<Client> hasNom(String nom) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(nom)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasEmail(String email) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(email)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasNumTel(String numTel) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(numTel)) {
                return cb.conjunction();
            }
            return cb.like(root.get("numTel"), "%" + numTel + "%");
        };
    }

    public static Specification<Client> belongsToEntreprise(Long idEntreprise) {
        return (root, query, cb) -> cb.equal(root.get("idEntreprise"), idEntreprise);
    }

    public static Specification<Client> searchByText(String searchText) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(searchText)) {
                return cb.conjunction();
            }
            String searchTerm = searchText.toLowerCase();
            return cb.or(
                    cb.like(cb.lower(root.get("nom")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(root.get("prenom")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(root.get("email")), "%" + searchTerm + "%"),
                    cb.like(root.get("numTel"), "%" + searchTerm + "%"),
                    cb.like(cb.lower(root.get("adresse").get("rue")), "%" + searchTerm + "%"),
                    cb.like(cb.lower(root.get("adresse").get("ville")), "%" + searchTerm + "%"),
                    cb.like(root.get("adresse").get("codePostal"), "%" + searchTerm + "%")
            );
        };
    }
}