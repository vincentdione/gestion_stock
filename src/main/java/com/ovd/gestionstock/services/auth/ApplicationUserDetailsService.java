package com.ovd.gestionstock.services.auth;


import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.models.ExtendedUser;
import com.ovd.gestionstock.services.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApplicationUserDetailsService implements UserDetailsService {

  @Autowired
  private UtilisateurService service;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UtilisateurDto utilisateur = service.findByUsername(username);
    if (utilisateur == null) {
      throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
    }
    if (utilisateur.getUsername() == null) {
      throw new IllegalArgumentException("Email ou mot de passe vide pour l'utilisateur : " + username);
    }
    if (utilisateur.getRole() == null || utilisateur.getRole().getAuthorities() == null) {
      throw new IllegalArgumentException("Aucune autorité définie pour l'utilisateur : " + username);
    }

    return new ExtendedUser(
            utilisateur.getEmail(),
            utilisateur.getPassword(),
            utilisateur.getEntrepriseDto() != null ? utilisateur.getEntrepriseDto().getId() : null,
            utilisateur.getRole().getAuthorities()
    );
  }

}
