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
@Table(name = "COMMANDE_CLIENTS")
@AllArgsConstructor
@NoArgsConstructor
public class CommandeClient {

    @Id
    @GeneratedValue
    private Long id;

    private String code;


    private Instant dateCommande;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @OneToMany(mappedBy = "commandeClient")
    private List<LigneCommandeClient> ligneCommandeClients = new ArrayList<>();



}
