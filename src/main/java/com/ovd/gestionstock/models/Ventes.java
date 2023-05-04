package com.ovd.gestionstock.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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

    private Instant dateVente;

    private String commentaire;

}
