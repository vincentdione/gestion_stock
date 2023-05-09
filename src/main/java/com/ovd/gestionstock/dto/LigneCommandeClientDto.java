package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.LigneCommandeClient;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;
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
public class LigneCommandeClientDto {

    private Long id;

    private ArticleDto articleDto;

    private CommandeClientDto commandeClientDto;

    public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommande){
        if(ligneCommande == null){
            return null;
        }
        return LigneCommandeClientDto.builder()
                .id(ligneCommande.getId())
                .build();
    }

    public static LigneCommandeClient toEntity(LigneCommandeClientDto ligneCommande){
        if(ligneCommande == null){
            return null;
        }
        return LigneCommandeClient.builder()
                .id(ligneCommande.getId())
                .build();
    }

}
