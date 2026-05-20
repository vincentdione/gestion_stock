package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Builder.Default
    private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = new ArrayList<>();

    public static CommandeFournisseurDto fromEntity(CommandeFournisseur commandeFournisseur) {
        if (commandeFournisseur == null) {
            return null;
        }

        CommandeFournisseurDto dto = CommandeFournisseurDto.builder()
                .id(commandeFournisseur.getId())
                .code(commandeFournisseur.getCode())
                .dateCommande(commandeFournisseur.getDateCommande())
                .fournisseurDto(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
                .etatCommande(commandeFournisseur.getEtatCommande())
                .idEntreprise(commandeFournisseur.getIdEntreprise())
                .modePayement(ModePayementDto.fromEntity(commandeFournisseur.getModePayement()))
                .build();

        // Ajout des lignes de commande fournisseur
        if (commandeFournisseur.getLigneCommandeFournisseurs() != null) {
            List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = commandeFournisseur.getLigneCommandeFournisseurs().stream()
                    .map(LigneCommandeFournisseurDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setLigneCommandeFournisseurDtos(ligneCommandeFournisseurDtos);
        }

        return dto;
    }

    public static CommandeFournisseur toEntity(CommandeFournisseurDto commandeFournisseurDto) {
        if (commandeFournisseurDto == null) {
            return null;
        }

        CommandeFournisseur commandeFournisseur = CommandeFournisseur.builder()
                .id(commandeFournisseurDto.getId())
                .code(commandeFournisseurDto.getCode())
                .dateCommande(commandeFournisseurDto.getDateCommande())
                .fournisseur(FournisseurDto.toEntity(commandeFournisseurDto.getFournisseurDto()))
                .etatCommande(commandeFournisseurDto.getEtatCommande())
                .idEntreprise(commandeFournisseurDto.getIdEntreprise())
                .modePayement(ModePayementDto.toEntity(commandeFournisseurDto.getModePayement()))
                .build();

        // Ajout des lignes de commande fournisseur à l'entité
        if (commandeFournisseurDto.getLigneCommandeFournisseurDtos() != null) {
            List<LigneCommandeFournisseur> ligneCommandeFournisseurs = commandeFournisseurDto.getLigneCommandeFournisseurDtos().stream()
                    .map(ligneDto -> {
                        LigneCommandeFournisseur ligne = LigneCommandeFournisseurDto.toEntity(ligneDto);
                        ligne.setCommandeFournisseur(commandeFournisseur); // Définit la relation bidirectionnelle
                        return ligne;
                    })
                    .collect(Collectors.toList());
            commandeFournisseur.setLigneCommandeFournisseurs(ligneCommandeFournisseurs);
        }

        return commandeFournisseur;
    }

    public boolean isCommandeLivree() {
        return CommandeEtat.LIVREE.equals(this.etatCommande);
    }
}