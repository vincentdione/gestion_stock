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

    private Long idEntreprise;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Unite unite;

}
