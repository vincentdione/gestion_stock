package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;
import com.ovd.gestionstock.models.LigneVente;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommandeFournisseurDto {

    private Long id;

    private ArticleDto article;

    private CommandeFournisseurDto commandeFournisseurDto;
    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    private Long idEntreprise;

    public static LigneCommandeFournisseurDto fromEntity(LigneCommandeFournisseur ligneCommandeFournisseur) {
        if (ligneCommandeFournisseur == null) {
            return null;
        }
        return LigneCommandeFournisseurDto.builder()
                .id(ligneCommandeFournisseur.getId())
                .article(ArticleDto.fromEntity(ligneCommandeFournisseur.getArticle()))
                .quantite(ligneCommandeFournisseur.getQuantite())
                .prixUnitaire(ligneCommandeFournisseur.getPrixUnitaire())
                .idEntreprise(ligneCommandeFournisseur.getIdEntreprise())
                .build();
    }

    public static LigneCommandeFournisseur toEntity(LigneCommandeFournisseurDto dto) {
        if (dto == null) {
            return null;
        }

        LigneCommandeFournisseur ligneCommandeFournisseur = new LigneCommandeFournisseur();
        ligneCommandeFournisseur.setId(dto.getId());
        ligneCommandeFournisseur.setArticle(ArticleDto.toEntity(dto.getArticle()));
        ligneCommandeFournisseur.setPrixUnitaire(dto.getPrixUnitaire());
        ligneCommandeFournisseur.setQuantite(dto.getQuantite());
        ligneCommandeFournisseur.setIdEntreprise(dto.getIdEntreprise());
        return ligneCommandeFournisseur;
    }
}
