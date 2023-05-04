package com.ovd.gestionstock.dto;

import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.Role;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.models.Ventes;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto {

    private Long id;

    private String username;
    private String password;
    private String email;
    private String nom;
    private String prenom;
    private EntrepriseDto entrepriseDto;

    public static UtilisateurDto fromEntity(Utilisateur utilisateur){
        if(utilisateur == null){
            return null;
        }
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .username(utilisateur.getUsername())
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDto utilisateurDto){
        if(utilisateurDto == null){
            return null;
        }
        return Utilisateur.builder()
                .id(utilisateurDto.getId())
                .nom(utilisateurDto.getNom())
                .email(utilisateurDto.getEmail())
                .prenom(utilisateurDto.getPrenom())
                .username(utilisateurDto.getUsername())
                .build();
    }

}
