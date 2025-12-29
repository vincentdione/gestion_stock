package com.ovd.gestionstock.models;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "ARTICLES")
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue
    private Long id;

    private String codeArticle;
    private String codeBarre;
    private String designation;
    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;
    private String photo;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSousCategory")
    private SousCategory sousCategory;

    @OneToMany(mappedBy = "article")
    private List<LigneVente> ligneVentes = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LigneCommandeClient> ligneCommandeClients = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<MvtStk> mvtStks = new ArrayList<>();


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUnite")
//    private Unite unite;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ConditionAV> conditions = new ArrayList<>();

//    public void addCondition(ConditionAV condition) {
//        conditions.add(condition);
//    }

    public void addCondition(ConditionAV condition) {
        System.out.println("addCondition === "+condition);
        conditions.add(condition);
        System.out.println("conditions === "+conditions.size());
        condition.setArticle(this);
    }

    public List<Unite> getUnites() {
        List<Unite> unites = new ArrayList<>();
        for (ConditionAV condition : conditions) {
            unites.add(condition.getUnite());
        }
        return unites;
    }

//    public double getPrixForUnite(Unite unite) {
//        for (ConditionAV condition : conditions) {
//            if (condition.getUnite().equals(unite)) {
//                return condition.getPrice();
//            }
//        }
//        return 0;
//    }


//    public Unite getUnitForSale() {
//        // Implémentez la logique pour choisir l'unité de vente en fonction des conditionnements
//
//        // Par exemple, si vous souhaitez vendre par pièce, vous pouvez faire :
//        return unites.stream()
//                .filter(unit -> unit.getNom().equals("piece"))
//                .findFirst()
//                .orElse(null);
//    }

}
