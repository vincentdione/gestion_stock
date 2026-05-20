package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Column(name = "nom_client")
    private String nomClient;

    @Column(name = "prenom_client")
    private String prenomClient;

    private String adresse;

    private String numero;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

    @OneToMany(mappedBy = "vente", fetch = FetchType.EAGER) // Changez en EAGER ou utilisez @EntityGraph
    private List<LigneVente> ligneVentes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModePayement")
    private ModePayement modePayement;

    // Ajoutez ce champ pour stocker le montant total
    @Transient
    private BigDecimal montantTotalCache = null;

    public BigDecimal getMontantTotal() {
        // Retourner le cache s'il existe
        if (montantTotalCache != null) {
            return montantTotalCache;
        }

        BigDecimal montantTotal = BigDecimal.ZERO;

        if (ligneVentes != null && !ligneVentes.isEmpty()) {
            for (LigneVente ligneVente : ligneVentes) {
                if (ligneVente.getPrixUnitaire() != null && ligneVente.getQuantite() != null) {
                    BigDecimal prixUnitaire = ligneVente.getPrixUnitaire();
                    BigDecimal quantite = new BigDecimal(String.valueOf(ligneVente.getQuantite()));
                    BigDecimal montantLigne = prixUnitaire.multiply(quantite);
                    montantTotal = montantTotal.add(montantLigne);
                }
            }
        }

        // Cache le résultat
        montantTotalCache = montantTotal;
        return montantTotal;
    }

    public int getNombreDeVentes() {
        if (ligneVentes != null) {
            return ligneVentes.size();
        }
        return 0;
    }

    // Méthode pour forcer le calcul
    public void calculerMontantTotal() {
        montantTotalCache = null;
        getMontantTotal();
    }
}