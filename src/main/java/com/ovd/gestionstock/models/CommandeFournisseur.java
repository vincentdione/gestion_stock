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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_seq")
    @SequenceGenerator(name = "code_seq", sequenceName = "SEQ_COMMANDE_FOURNISSEUR", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String code;

    private Instant dateCommande;

    @Enumerated(EnumType.STRING)
    private  CommandeEtat etatCommande;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;


    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseur")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModePayement")
    private ModePayement modePayement;

    @PrePersist
    private void generateCode() {
        if (this.code == null) {
            // Générez le code au format "CMD-FN000000X" en utilisant la séquence
            String sequenceValue = String.format("%07d", this.id);
            this.code = "CMD-FN" + sequenceValue;
        }
    }

}
