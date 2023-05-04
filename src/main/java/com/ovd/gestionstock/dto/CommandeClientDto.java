package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.LigneCommandeClient;
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

    private List<LigneCommandeClientDto> ligneCommandeClientDtos = new ArrayList<>();

    public CommandeClientDto fromEntity(CommandeClientDto commandeClientDto) {
        if (commandeClientDto == null) {
            return null;
        }
        return  CommandeClientDto.builder()
                .id(commandeClientDto.getId())
                .code(commandeClientDto.getCode())
                .dateCommande(commandeClientDto.getDateCommande())
                .clientDto(commandeClientDto.getClientDto())
                .build();
    }


    public CommandeClient toEntity(CommandeClient commandeClient) {
        if (commandeClient == null) {
            return null;
        }
        return  CommandeClient.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommande(commandeClient.getDateCommande())
                .build();
    }

}
