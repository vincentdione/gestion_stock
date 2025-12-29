package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "CONDITION_AV")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConditionAV {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal quantite;

    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTval;
    private BigDecimal prixUnitaireTtc;

    // Autres attributs de la conditionnement

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    private Unite unite;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

}
