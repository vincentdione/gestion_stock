package com.ovd.gestionstock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ovd.gestionstock.models.Adresse;
import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.models.CommandeClient;
import jakarta.persistence.Embedded;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private Long id;

    private String nom;

    private String prenom;

    private Adresse adresse;

    private String photo;
    private String email;
    private String numTel;

    @JsonIgnore
    private List<CommandeClientDto> commandeClientDtos = new ArrayList<>();

    public static ClientDto fromEntity(Client client){
        if(client == null){
            return null;
        }
        return ClientDto.builder()
                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .numTel(client.getNumTel())
                .adresse(client.getAdresse())
                .photo(client.getPhoto())
                .build();
    }

    public static Client toEntity(ClientDto clientDto){
        if(clientDto == null){
            return null;
        }
        return Client.builder()
                .id(clientDto.getId())
                .nom(clientDto.getNom())
                .prenom(clientDto.getPrenom())
                .email(clientDto.getEmail())
                .numTel(clientDto.getNumTel())
                .adresse(clientDto.getAdresse())
                .photo(clientDto.getPhoto())
                .build();
    }


}
