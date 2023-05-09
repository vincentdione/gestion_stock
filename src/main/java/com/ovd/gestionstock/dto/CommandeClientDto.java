package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.*;
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
public class CommandeClientDto {

    private Long id;

    private String code;


    private Instant dateCommande;

    private ClientDto clientDto;

    private CommandeEtat etat;

    @JsonIgnore
    private List<LigneCommandeClientDto> ligneCommandeClientDtos = new ArrayList<>();

    public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
        if (commandeClient == null) {
            return null;
        }
        return  CommandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommande(commandeClient.getDateCommande())
                .clientDto(ClientDto.fromEntity(commandeClient.getClient()))
                .etat(commandeClient.getEtat())
                .build();
    }


    public static CommandeClient toEntity(CommandeClientDto commandeClientDto) {
        if (commandeClientDto == null) {
            return null;
        }
        return  CommandeClient.builder()
                .id(commandeClientDto.getId())
                .code(commandeClientDto.getCode())
                .dateCommande(commandeClientDto.getDateCommande())
                .etat(commandeClientDto.getEtat())
                .build();
    }

    public boolean isCommandeLivree(){
        return CommandeEtat.LIVREE.equals(this.etat);
    }

}
