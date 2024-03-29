package com.ovd.gestionstock.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangerMotDePasseUtilisateurDto {

  private Long id;

  private String motDePasse;

  private String confirmMotDePasse;

}
