package com.ovd.gestionstock.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "SOUSCATEGORIES")
@AllArgsConstructor
@NoArgsConstructor
public class SousCategory implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String designation;

    @OneToMany(mappedBy = "sousCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcategory")
    private Category category;

}
