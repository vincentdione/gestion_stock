package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Adresse;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.Livraison;
import com.ovd.gestionstock.models.LivraisonEtat;
import com.ovd.gestionstock.models.Utilisateur;
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
    private CommandeClientDto commandeClient;
    private Adresse adresse;
    private UtilisateurDto utilisateur;
    private Long idEntreprise;




    public static LivraisonDto fromEntity(Livraison livraison){
        if(livraison == null){
            return null;
        }
        return LivraisonDto.builder()
                .id(livraison.getId())
                .code(livraison.getCode())
                .dateLivraison(livraison.getDateLivraison())
                .etat(livraison.getEtat())
                .adresse(livraison.getAdresse())
                .idEntreprise(livraison.getIdEntreprise())
                .commandeClient(CommandeClientDto.fromEntity(livraison.getCommandeClient()))
                .utilisateur(UtilisateurDto.fromEntity(livraison.getUtilisateur()))
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
                .adresse(dto.getAdresse())
                .idEntreprise(dto.getIdEntreprise())
                .commandeClient(CommandeClientDto.toEntity(dto.getCommandeClient()))
                .utilisateur(UtilisateurDto.toEntity(dto.getUtilisateur()))
                .build();
    }

}
