package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.models.Fournisseur;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeFournisseurDto {

    private Long id;

    private String code;

    private Instant dateCommande;

    private FournisseurDto fournisseurDto;

    private CommandeEtat etatCommande;

    private Long idEntreprise;
    private ModePayementDto modePayement;


    private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = new ArrayList<>();

    public static CommandeFournisseurDto fromEntity(CommandeFournisseur commandeFournisseur) {
        if (commandeFournisseur == null) {
            return null;
        }
        return  CommandeFournisseurDto.builder()
                .id(commandeFournisseur.getId())
                .code(commandeFournisseur.getCode())
                .dateCommande(commandeFournisseur.getDateCommande())
                .fournisseurDto(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
                .etatCommande(commandeFournisseur.getEtatCommande())
                .idEntreprise(commandeFournisseur.getIdEntreprise())
                .modePayement(ModePayementDto.fromEntity(commandeFournisseur.getModePayement()))
                .build();
    }

    public static CommandeFournisseur toEntity(CommandeFournisseurDto commandeFournisseur) {
        if (commandeFournisseur == null) {
            return null;
        }
        return  CommandeFournisseur.builder()
                .id(commandeFournisseur.getId())
                .code(commandeFournisseur.getCode())
                .dateCommande(commandeFournisseur.getDateCommande())
                .fournisseur(FournisseurDto.toEntity(commandeFournisseur.getFournisseurDto()))
                .etatCommande(commandeFournisseur.getEtatCommande())
                .idEntreprise(commandeFournisseur.getIdEntreprise())
                .modePayement(ModePayementDto.toEntity(commandeFournisseur.getModePayement()))
                .build();
    }
    public boolean isCommandeLivree(){
        return CommandeEtat.LIVREE.equals(this.etatCommande);
    }
}
