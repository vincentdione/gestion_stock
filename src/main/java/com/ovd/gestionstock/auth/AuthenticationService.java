package com.ovd.gestionstock.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovd.gestionstock.config.JwtService;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.EntrepriseRepository;
import com.ovd.gestionstock.repositories.UtilisateurRepository;
import com.ovd.gestionstock.token.Token;
import com.ovd.gestionstock.token.TokenRepository;
import com.ovd.gestionstock.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
  private final UtilisateurRepository repository;
  private final EntrepriseRepository entrepriseRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    Entreprise entreprise = entrepriseRepository.findById(request.getEntrepriseId())
            .orElseThrow(() -> new InvalidEntityException("Entreprise non trouvée", ErrorCodes.ENTREPRISE_NOT_FOUND));

    var user = Utilisateur.builder()
            .nom(request.getNom())
            .prenom(request.getPrenom())
            .username(request.getUsername())
            .email(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .entreprise(entreprise) // Associer l'entreprise ici
            .build();

    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    String roleName = "ROLE_" + user.getRole().name();


    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .entrepriseNom(entreprise.getNom()) // Retourner les informations de l'entreprise
            .roles(Collections.singletonList(roleName))  // Ajout du nom du rôle principal à la réponse
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // Ajoutez des logs de débogage
    log.debug("Tentative d'authentification pour: {}", request.getUsername());

    try {
      // Authentification via Spring Security
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getUsername(),
                      request.getPassword()
              )
      );

      // Récupération de l'utilisateur
      var user = repository.findByUsername(request.getUsername())
              .orElseThrow(() -> {
                log.error("Utilisateur non trouvé: {}", request.getUsername());
                return new InvalidEntityException("Utilisateur non trouvé", ErrorCodes.UTILISATEUR_NOT_FOUND);
              });

      // Vérification de l'entreprise
      if (user.getEntreprise() == null || !user.getEntreprise().isActive()) {
        log.warn("Entreprise inactive ou non définie pour l'utilisateur: {}", request.getUsername());
        throw new DisabledException("Compte désactivé - Entreprise inactive");
      }

      // Génération des tokens
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);

      // Gestion des tokens existants
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      log.info("Authentification réussie pour: {}", request.getUsername());

      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .entrepriseNom(user.getEntreprise().getNom())
              .roles(Collections.singletonList("ROLE_" + user.getRole().name()))
              .build();

    } catch (BadCredentialsException e) {
      log.error("Identifiants invalides pour: {}", request.getUsername());
      throw new InvalidEntityException("Identifiants invalides", ErrorCodes.UTILISATEUR_NOT_FOUND);
    }
  }

//  public AuthenticationResponse authenticate(AuthenticationRequest request) {
//    authenticationManager.authenticate(
//        new UsernamePasswordAuthenticationToken(
//            request.getUsername(),
//            request.getPassword()
//        )
//    );
//    var userDetails = repository.findByUsername(request.getUsername());
//    if (userDetails.isEmpty()){
//      throw new InvalidEntityException("Utilisateur not found", ErrorCodes.UTILISATEUR_NOT_FOUND);
//    }
//    Utilisateur user = userDetails.get();
//    var jwtToken = jwtService.generateToken(user);
//    var refreshToken = jwtService.generateRefreshToken(user);
//    revokeAllUserTokens(user);
//    saveUserToken(user, jwtToken);
//
//    String roleName = "ROLE_" + user.getRole().name();
//
//
//    return AuthenticationResponse.builder()
//        .accessToken(jwtToken)
//            .refreshToken(refreshToken)
//            .entrepriseNom(user.getEntreprise().getNom())
//            .roles(Collections.singletonList(roleName))  // Ajout du nom du rôle principal à la réponse
//        .build();
//  }

  private void saveUserToken(Utilisateur utilisateur, String jwtToken) {
    var token = Token.builder()
        .utilisateur(utilisateur)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(Utilisateur user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void findUserByToken(Token token){
     if (token == null){
       log.error("Token est null");
       throw new InvalidEntityException("Token n'est pas valide",ErrorCodes.UTILISATEUR_NOT_FOUND);
     }

  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByUsername(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
