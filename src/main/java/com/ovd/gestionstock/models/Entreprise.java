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
@Table(name = "ENTREPRISES")
@AllArgsConstructor
@NoArgsConstructor
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String codeFiscal;
    private String siteWeb;
    private boolean active = true;


    @Embedded
    private Adresse adresse;

    private String email;
    private String numTel;

    @OneToMany(mappedBy = "entreprise")
    private List<Utilisateur> utilisateurs =new ArrayList<>();


}
