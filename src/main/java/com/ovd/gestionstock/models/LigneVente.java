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
@Table(name = "LIGNE_VENTES")
@AllArgsConstructor
@NoArgsConstructor
public class LigneVente {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idVente")
    private Ventes vente;

    private BigDecimal quantite;
    private BigDecimal prixUnitaire;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

    private String unite;


    @ManyToOne
    private Article article;


}
