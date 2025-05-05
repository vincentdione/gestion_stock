package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "FOURNISSEURS")
@AllArgsConstructor
@NoArgsConstructor
public class Fournisseur {

    @Id
    @GeneratedValue
    private Long id;

    private String nom;

    private String prenom;

    @Embedded
    private Adresse adresse;

    private String photo;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String numTel;

    @OneToMany(mappedBy = "fournisseur")
    private List<CommandeFournisseur> commandeFournisseurs = new ArrayList<>();

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

}
