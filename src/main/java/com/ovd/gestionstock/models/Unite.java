package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UNITES")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Unite {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nom;
    private String designation;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

//    @OneToMany(mappedBy = "unite", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Article> articles = new ArrayList<>();


}
