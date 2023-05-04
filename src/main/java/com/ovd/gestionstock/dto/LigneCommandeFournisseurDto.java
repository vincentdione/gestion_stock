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

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneCommandeFournisseurDto {

    private Long id;

    private ArticleDto articleDto;

    private CommandeFournisseurDto commandeFournisseurDto;


    public static LigneCommandeFournisseurDto fromEntity(LigneCommandeFournisseur ligneCommandeFournisseur){
        if(ligneCommandeFournisseur == null){
            return null;
        }
        return LigneCommandeFournisseurDto.builder()
                .id(ligneCommandeFournisseur.getId())
                .build();
    }


    public static LigneCommandeFournisseur toEntity(LigneCommandeFournisseurDto ligneCommandeFournisseurDto){
        if(ligneCommandeFournisseurDto == null){
            return null;
        }
        return LigneCommandeFournisseur.builder()
                .id(ligneCommandeFournisseurDto.getId())
                .build();
    }
}
