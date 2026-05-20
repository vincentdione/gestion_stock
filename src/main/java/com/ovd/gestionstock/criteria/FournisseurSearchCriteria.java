package com.ovd.gestionstock.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurSearchCriteria {
    private String nom;
    private String prenom;
    private String email;
    private String numTel;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;
}