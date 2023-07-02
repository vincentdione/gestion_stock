package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "COMMANDE_CLIENTS")
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandeClient {

    @Id
    @GeneratedValue
    private Long id;

    private String code;


    private Date dateCommande;

    @Enumerated(EnumType.STRING)
    private CommandeEtat etat;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @OneToMany(mappedBy = "commandeClient")
    private List<LigneCommandeClient> ligneCommandeClients = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModePayement")
    private ModePayement modePayement;

}
