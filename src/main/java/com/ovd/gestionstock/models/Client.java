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
@Table(name = "CLIENTS")
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    private String nom;

    private String prenom;

    @Embedded
    private Adresse adresse;

    private String photo;
    private String email;
    private String numTel;

    private Long idEntreprise;


    @OneToMany(mappedBy = "client")
    private List<CommandeClient> commandeClients = new ArrayList<>();


}
