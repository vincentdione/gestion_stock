package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Fournisseur;
import com.ovd.gestionstock.models.Ventes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenteDto {

    private Long id;

    private String code;

    private Instant dateVente;

    private String commentaire;

    public static VenteDto fromEntity(Ventes ventes){
        if(ventes == null){
            return null;
        }
        return VenteDto.builder()
                .id(ventes.getId())
                .code(ventes.getCode())
                .dateVente(ventes.getDateVente())
                .commentaire(ventes.getCommentaire())
                .build();
    }


    public static Ventes toEntity(VenteDto venteDto){
        if(venteDto == null){
            return null;
        }
        return Ventes.builder()
                .id(venteDto.getId())
                .code(venteDto.getCode())
                .dateVente(venteDto.getDateVente())
                .commentaire(venteDto.getCommentaire())
                .build();
    }

}
