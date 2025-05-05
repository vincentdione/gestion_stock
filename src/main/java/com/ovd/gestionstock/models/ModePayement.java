package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MODE_PAYEMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModePayement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String code;
    private String designation;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

    @OneToMany(mappedBy = "modePayement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ventes> ventes = new ArrayList<>();

    @OneToMany(mappedBy = "modePayement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeClient> commandeClients = new ArrayList<>();

    @OneToMany(mappedBy = "modePayement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeFournisseur> commandeFournisseurs = new ArrayList<>();

}
