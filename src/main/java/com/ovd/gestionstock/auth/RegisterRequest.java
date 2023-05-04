package com.ovd.gestionstock.auth;


import com.ovd.gestionstock.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String nom;
  private String prenom;
  private String username;
  private String email;
  private String password;
  private Role role;
}
