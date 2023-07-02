package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Adresse;
import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.models.Fournisseur;
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
public class FournisseurDto {

    private Long id;

    private String nom;

    private String prenom;
    private AdresseDto adresseDto;

    private String photo;
    private String email;
    private String numTel;

    private List<CommandeFournisseurDto> commandeFournisseurDtos = new ArrayList<>();

    public static FournisseurDto fromEntity(Fournisseur fournisseur){
        if(fournisseur == null){
            return null;
        }
        return FournisseurDto.builder()
                .id(fournisseur.getId())
                .nom(fournisseur.getNom())
                .prenom(fournisseur.getPrenom())
                .email(fournisseur.getEmail())
                .numTel(fournisseur.getNumTel())
                .adresseDto(AdresseDto.fromEntity(fournisseur.getAdresse()))
                .photo(fournisseur.getPhoto())
                .build();
    }

    public static Fournisseur toEntity(FournisseurDto fournisseurDto){
        if(fournisseurDto == null){
            return null;
        }
        return Fournisseur.builder()
                .id(fournisseurDto.getId())
                .nom(fournisseurDto.getNom())
                .prenom(fournisseurDto.getPrenom())
                .email(fournisseurDto.getEmail())
                .numTel(fournisseurDto.getNumTel())
                .adresse(AdresseDto.toEntity(fournisseurDto.getAdresseDto()))
                .photo(fournisseurDto.getPhoto())
                .build();
    }

}
