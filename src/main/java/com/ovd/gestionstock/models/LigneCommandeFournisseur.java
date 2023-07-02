package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@Table(name = "LIGNE_COMMANDE_FOURNISSEURS")
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommandeFournisseur {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idArticle")
    private Article article;

    private Long idEntreprise;

    private BigDecimal quantite;


    @Column(name = "prixunitaire")
    private BigDecimal prixUnitaire;




    @ManyToOne
    @JoinColumn(name = "idCommandeFournisseur")
    private CommandeFournisseur commandeFournisseur;
}
