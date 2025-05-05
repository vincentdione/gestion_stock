package com.ovd.gestionstock.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "CLIENTS")
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client {

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

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;


    @OneToMany(mappedBy = "client")
    private List<CommandeClient> commandeClients = new ArrayList<>();


}
