package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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
    @GeneratedValue
    private Long id;

    private String code;

    private Date dateVente;

    private String commentaire;
    @Column(name = "identreprise")
    private Long idEntreprise;

    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVentes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModePayement")
    private ModePayement modePayement;

}
