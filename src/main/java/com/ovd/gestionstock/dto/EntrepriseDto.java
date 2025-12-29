package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Adresse;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.FileInfo;
import com.ovd.gestionstock.models.Utilisateur;
import jakarta.persistence.Embedded;
import jakarta.persistence.Lob;
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
public class EntrepriseDto {

    private Long id;

    private String nom;
    private FileInfo logo;
    private String description;
    private String codeFiscal;
    private String siteWeb;
    private AdresseDto adresseDto;

    private String email;
    private String numTel;

    private List<UtilisateurDto> utilisateurDtos =new ArrayList<>();

    public static EntrepriseDto fromEntity(Entreprise entreprise) {
        if (entreprise == null) {
            return null;
        }
        return  EntrepriseDto.builder()
                .id(entreprise.getId())
                .codeFiscal(entreprise.getCodeFiscal())
                .nom(entreprise.getNom())
                .logo(entreprise.getLogo())
                .email(entreprise.getEmail())
                .numTel(entreprise.getNumTel())
                .adresseDto(AdresseDto.fromEntity(entreprise.getAdresse()))
                .siteWeb(entreprise.getSiteWeb())
                .description(entreprise.getDescription())
                .build();
    }


    public static Entreprise toEntity(EntrepriseDto entrepriseDto) {
        if (entrepriseDto == null) {
            return null;
        }
        return  Entreprise.builder()
                .id(entrepriseDto.getId())
                .codeFiscal(entrepriseDto.getCodeFiscal())
                .nom(entrepriseDto.getNom())
                .logo(entrepriseDto.getLogo())
                .email(entrepriseDto.getEmail())
                .numTel(entrepriseDto.getNumTel())
                .adresse(AdresseDto.toEntity(entrepriseDto.getAdresseDto()))
                .siteWeb(entrepriseDto.getSiteWeb())
                .description(entrepriseDto.getDescription())
                .build();
    }
}
