package com.ovd.gestionstock.dto;

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

    private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = new ArrayList<>();

    public CommandeFournisseurDto fromEntity(CommandeFournisseurDto commandeFournisseurDto) {
        if (commandeFournisseurDto == null) {
            return null;
        }
        return  CommandeFournisseurDto.builder()
                .id(commandeFournisseurDto.getId())
                .code(commandeFournisseurDto.getCode())
                .dateCommande(commandeFournisseurDto.getDateCommande())
                .fournisseurDto(commandeFournisseurDto.getFournisseurDto())
                .build();
    }

    public CommandeFournisseur toEntity(CommandeFournisseur commandeFournisseur) {
        if (commandeFournisseur == null) {
            return null;
        }
        return  CommandeFournisseur.builder()
                .id(commandeFournisseur.getId())
                .code(commandeFournisseur.getCode())
                .dateCommande(commandeFournisseur.getDateCommande())
                .fournisseur(commandeFournisseur.getFournisseur())
                .build();
    }

}
