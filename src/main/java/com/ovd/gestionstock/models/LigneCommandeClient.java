package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity
@Table(name = "LIGNE_COMMANDE_CLIENTS")
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommandeClient {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idArticle")
    private Article article;

    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "idCommandeClient")
    private CommandeClient commandeClient;

}
