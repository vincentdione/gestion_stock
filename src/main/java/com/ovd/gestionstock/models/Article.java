package com.ovd.gestionstock.models;


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
    private String designation;
    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;
    private String photo;

    private Long idEntreprise;

    @ManyToOne
    @JoinColumn(name = "idCategory")
    private Category category;

    @OneToMany(mappedBy = "article")
    private List<LigneVente> ligneVentes = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LigneCommandeClient> ligneCommandeClients = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<MvtStk> mvtStks = new ArrayList<>();

}
