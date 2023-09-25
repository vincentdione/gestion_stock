package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "VENTES")
@AllArgsConstructor
@NoArgsConstructor
public class Ventes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_seq")
    @SequenceGenerator(name = "code_seq", sequenceName = "SEQ_COMMANDE_VENTE", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String code;

    private Date dateVente;

    private String commentaire;
    @Column(name = "identreprise")
    private Long idEntreprise;

    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVentes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModePayement")
    private ModePayement modePayement;


    public BigDecimal getMontantTotal() {
        BigDecimal montantTotal = BigDecimal.ZERO;
        System.out.println("///////////////////////////////////////////////"+ligneVentes.size());

        if (ligneVentes != null) {
            for (LigneVente ligneVente : ligneVentes) {
                if (ligneVente.getPrixUnitaire() != null && ligneVente.getQuantite() != null) {
                    BigDecimal prixUnitaire = new BigDecimal(String.valueOf(ligneVente.getPrixUnitaire()));
                    BigDecimal quantite = new BigDecimal(String.valueOf(ligneVente.getQuantite()));
                    BigDecimal montantLigne = prixUnitaire.multiply(quantite);
                    System.out.println("///////////////////////////////////////////////");
                    System.out.println(prixUnitaire);
                    System.out.println(quantite);
                    System.out.println(montantLigne);
                    System.out.println("///////////////////////////////////////////////");

                    montantTotal = montantTotal.add(montantLigne);

                }
            }
        }
        return montantTotal;
    }


    public int getNombreDeVentes() {
        if (ligneVentes != null) {
            return ligneVentes.size();
        }
        return 0;
    }

}
