package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.CommandeEtat;
import com.ovd.gestionstock.models.LigneCommandeClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeClientDto {

    private Long id;
    private String code;
    private Date dateCommande;
    private ClientDto clientDto;
    private CommandeEtat etat;
    private ModePayementDto modePayement;
    private Long idEntreprise;

    @Builder.Default
    private List<LigneCommandeClientDto> ligneCommandeClients = new ArrayList<>();

    public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
        if (commandeClient == null) {
            return null;
        }

        CommandeClientDto dto = CommandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommande(commandeClient.getDateCommande())
                .clientDto(ClientDto.fromEntity(commandeClient.getClient()))
                .idEntreprise(commandeClient.getIdEntreprise())
                .etat(commandeClient.getEtat())
                .modePayement(ModePayementDto.fromEntity(commandeClient.getModePayement()))
                .build();

        // Ajout des lignes de commande
        if (commandeClient.getLigneCommandeClients() != null) {
            List<LigneCommandeClientDto> ligneCommandeClientDtos = commandeClient.getLigneCommandeClients().stream()
                    .map(LigneCommandeClientDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setLigneCommandeClients(ligneCommandeClientDtos);
        }

        return dto;
    }

    public static CommandeClient toEntity(CommandeClientDto commandeClientDto) {
        if (commandeClientDto == null) {
            return null;
        }

        CommandeClient commandeClient = CommandeClient.builder()
                .id(commandeClientDto.getId())
                .code(commandeClientDto.getCode())
                .dateCommande(commandeClientDto.getDateCommande())
                .client(ClientDto.toEntity(commandeClientDto.getClientDto()))
                .idEntreprise(commandeClientDto.getIdEntreprise())
                .etat(commandeClientDto.getEtat())
                .modePayement(ModePayementDto.toEntity(commandeClientDto.getModePayement()))
                .build();

        // Ajout des lignes de commande à l'entité
        if (commandeClientDto.getLigneCommandeClients() != null) {
            List<LigneCommandeClient> ligneCommandeClients = commandeClientDto.getLigneCommandeClients().stream()
                    .map(ligneDto -> {
                        LigneCommandeClient ligne = LigneCommandeClientDto.toEntity(ligneDto);
                        ligne.setCommandeClient(commandeClient);
                        return ligne;
                    })
                    .collect(Collectors.toList());
            commandeClient.setLigneCommandeClients(ligneCommandeClients);
        }

        return commandeClient;
    }

    public boolean isCommandeLivree() {
        return CommandeEtat.LIVREE.equals(this.etat);
    }
}