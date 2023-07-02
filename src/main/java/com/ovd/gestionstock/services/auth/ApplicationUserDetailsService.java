package com.ovd.gestionstock.services.auth;


import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.models.ExtendedUser;
import com.ovd.gestionstock.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

  @Autowired
  private UtilisateurService service;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UtilisateurDto utilisateur = service.findByUsername(username);

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    return new ExtendedUser(utilisateur.getEmail(), utilisateur.getPassword(), utilisateur.getEntrepriseDto().getId(), utilisateur.getRole().getAuthorities());
  }
}
