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
@Table(name = "CATEGORIES")
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String designation;

    @OneToMany(mappedBy = "category")
    private List<Article> articles = new ArrayList<>();

}
