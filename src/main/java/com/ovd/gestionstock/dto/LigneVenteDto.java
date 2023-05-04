package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.LigneVente;
import com.ovd.gestionstock.models.MvtStk;
import com.ovd.gestionstock.models.Ventes;
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
public class LigneVenteDto {

    private Long id;

    private VenteDto venteDto;

    private BigDecimal quantite;
    private BigDecimal prixUnitaire;

    public static LigneVenteDto fromEntity(LigneVente ligneVente){
        if(ligneVente == null){
            return null;
        }
        return LigneVenteDto.builder()
                .id(ligneVente.getId())
                .quantite(ligneVente.getQuantite())
                .prixUnitaire(ligneVente.getPrixUnitaire())
                .build();
    }

    public static LigneVente toEntity(LigneVenteDto ligneVenteDto){
        if(ligneVenteDto == null){
            return null;
        }
        return LigneVente.builder()
                .id(ligneVenteDto.getId())
                .quantite(ligneVenteDto.getQuantite())
                .prixUnitaire(ligneVenteDto.getPrixUnitaire())
                .build();
    }

}
