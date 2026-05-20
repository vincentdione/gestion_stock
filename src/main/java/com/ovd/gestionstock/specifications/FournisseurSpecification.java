package com.ovd.gestionstock.specifications;

import com.ovd.gestionstock.criteria.FournisseurSearchCriteria;
import com.ovd.gestionstock.models.Fournisseur;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurSpecification {

    public static Specification<Fournisseur> withCriteria(FournisseurSearchCriteria criteria, Long idEntreprise) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
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

            // Recherche par adresse (recherche dans rue, ville, code postal, pays)
            if (StringUtils.hasText(criteria.getAdresse())) {
                String searchTerm = criteria.getAdresse().toLowerCase();
                Predicate adressePredicate = cb.or(
                        cb.like(cb.lower(root.get("adresse").get("rue")), "%" + searchTerm + "%"),
                        cb.like(cb.lower(root.get("adresse").get("ville")), "%" + searchTerm + "%"),
                        cb.like(root.get("adresse").get("codePostal"), "%" + criteria.getAdresse() + "%"),
                        cb.like(cb.lower(root.get("adresse").get("pays")), "%" + searchTerm + "%")
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

            // Recherche spécifique par pays
            if (StringUtils.hasText(criteria.getPays())) {
                predicates.add(cb.like(
                        cb.lower(root.get("adresse").get("pays")),
                        "%" + criteria.getPays().toLowerCase() + "%"
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
    public static Specification<Fournisseur> hasNom(String nom) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(nom)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%");
        };
    }

    public static Specification<Fournisseur> hasPrenom(String prenom) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(prenom)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("prenom")), "%" + prenom.toLowerCase() + "%");
        };
    }

    public static Specification<Fournisseur> hasEmail(String email) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(email)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<Fournisseur> hasNumTel(String numTel) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(numTel)) {
                return cb.conjunction();
            }
            return cb.like(root.get("numTel"), "%" + numTel + "%");
        };
    }

    public static Specification<Fournisseur> hasVille(String ville) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(ville)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("adresse").get("ville")), "%" + ville.toLowerCase() + "%");
        };
    }

    public static Specification<Fournisseur> belongsToEntreprise(Long idEntreprise) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.equal(root.get("idEntreprise"), idEntreprise);
    }

    public static Specification<Fournisseur> searchByText(String searchText) {
        return (Root<Fournisseur> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
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
                    cb.like(root.get("adresse").get("codePostal"), "%" + searchTerm + "%"),
                    cb.like(cb.lower(root.get("adresse").get("pays")), "%" + searchTerm + "%")
            );
        };
    }
}