package com.ovd.gestionstock.config;


import com.ovd.gestionstock.services.auth.ApplicationUserDetailsService;
import com.ovd.gestionstock.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final TokenRepository tokenRepository;
  private final ApplicationUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    log.debug("Début JwtAuthenticationFilter - URI: {}", request.getRequestURI());

    // Laisser passer les appels d'auth
    if (request.getServletPath().contains("/api/v1/auth")) {
      log.debug("URI publique détectée, on passe le filtre JWT.");
      filterChain.doFilter(request, response);
      return;
    }

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.warn("En-tête Authorization manquant ou mal formé: {}", authHeader);
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7).trim();
    if (jwt.isEmpty() || jwt.split("\\.").length != 3) {
      log.error("JWT vide ou malformé détecté : {}", jwt);
      filterChain.doFilter(request, response);
      return;
    }

    try {
      userEmail = jwtService.extractUsername(jwt);
      log.debug("Email extrait du JWT : {}", userEmail);
    } catch (Exception e) {
      log.error("Erreur lors de l'extraction de l'email depuis le JWT", e);
      filterChain.doFilter(request, response);
      return;
    }

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        log.debug("Chargement de l'utilisateur depuis UserDetailsService réussi");

        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        log.debug("Vérification token en base : {}", isTokenValid);

        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          log.info("JWT valide pour l'utilisateur {}", userEmail);
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          log.warn("JWT invalide ou révoqué/expiré pour l'utilisateur {}", userEmail);
        }

      } catch (Exception ex) {
        log.error("Erreur lors de la validation ou de l'authentification avec JWT", ex);
      }
    } else {
      if (SecurityContextHolder.getContext().getAuthentication() != null) {
        log.debug("Contexte de sécurité déjà présent pour : {}", userEmail);
      } else {
        log.warn("Aucun email trouvé dans le JWT.");
      }
    }

    filterChain.doFilter(request, response);
  }
}
