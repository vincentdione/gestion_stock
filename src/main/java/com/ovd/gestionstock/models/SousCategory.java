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

    @Column(unique = true)
    private String code;

    private String designation;

    @OneToMany(mappedBy = "sousCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Article> articles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcategory")
    private Category category;

    @Column(name = "id_entreprise", nullable = false)
    private Long idEntreprise;

    // Méthodes helper pour gérer la collection
    public void addArticle(Article article) {
        articles.add(article);
        article.setSousCategory(this);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
        article.setSousCategory(null);
    }

    // Setter personnalisé pour éviter le problème de référence
    public void setArticles(List<Article> articles) {
        if (this.articles == null) {
            this.articles = articles;
        } else if (this.articles != articles) { // Vérifier si c'est une référence différente
            this.articles.clear();
            if (articles != null) {
                this.articles.addAll(articles);
                articles.forEach(article -> article.setSousCategory(this));
            }
        }
    }
}