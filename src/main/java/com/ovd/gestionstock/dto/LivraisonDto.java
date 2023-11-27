package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.Livraison;
import com.ovd.gestionstock.models.LivraisonEtat;
import com.ovd.gestionstock.models.MvtStk;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDto {

    private Long id;
    private String code;

    private Date dateLivraison;
    private LivraisonEtat etat;
    private CommandeClient commandeClient;




    public static LivraisonDto fromEntity(Livraison livraison){
        if(livraison == null){
            return null;
        }
        return LivraisonDto.builder()
                .id(livraison.getId())
                .code(livraison.getCode())
                .dateLivraison(livraison.getDateLivraison())
                .etat(livraison.getEtat())
                .commandeClient(livraison.getCommandeClient())
                .build();
    }

    public static Livraison toEntity(LivraisonDto dto){
        if(dto == null){
            return null;
        }

        return Livraison.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .dateLivraison(dto.getDateLivraison())
                .etat(dto.getEtat())
                .commandeClient(dto.getCommandeClient())
                .build();
    }

}
