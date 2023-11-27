package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "LIVRAISONS")
@AllArgsConstructor
@NoArgsConstructor
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "livraison_seq")
    @SequenceGenerator(name = "livraison_seq", sequenceName = "SEQ_LIVRAISON", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private Date dateLivraison;

    @Enumerated(EnumType.STRING)
    private LivraisonEtat etat;

    @ManyToOne
    @JoinColumn(name = "idCommandeClient")
    private CommandeClient commandeClient;


    @PrePersist
    private void generateCode() {
        if (this.code == null) {
            // Générez le code au format "LIV-FN000000X" en utilisant la séquence
            String sequenceValue = String.format("%07d", this.id);
            this.code = "LIVCL" + sequenceValue;
        }
    }
}
