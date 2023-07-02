package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    private double price;

    // Autres attributs de la conditionnement

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    private Unite unite;

}
