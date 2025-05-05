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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_seq")
    @SequenceGenerator(name = "code_seq", sequenceName = "SEQ_COMMANDE_CLIENT", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
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

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

    @PrePersist
    private void generateCode() {
        if (this.code == null) {
            // Générez le code au format "CMD-FN000000X" en utilisant la séquence
            String sequenceValue = String.format("%07d", this.id);
            this.code = "CMD-CL" + sequenceValue;
        }
    }

}
