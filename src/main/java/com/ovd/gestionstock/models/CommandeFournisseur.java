package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "COMMANDE_FOURNISSEURS")
@AllArgsConstructor
@NoArgsConstructor
public class CommandeFournisseur {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private Instant dateCommande;

    @Enumerated(EnumType.STRING)
    private  CommandeEtat etatCommande;

    private Long idEntreprise;


    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseur")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModePayement")
    private ModePayement modePayement;

}
