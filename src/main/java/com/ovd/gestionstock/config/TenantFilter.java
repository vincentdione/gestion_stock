package com.ovd.gestionstock.config;

import com.ovd.gestionstock.models.Utilisateur;
import com.ovd.gestionstock.repositories.UtilisateurRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            log.debug("Début TenantFilter - URI: {}", request.getRequestURI());

            // Skip pour les endpoints publics
            if (shouldSkipTenantCheck(request)) {
                log.debug("Skipping tenant check pour URI publique");
                filterChain.doFilter(request, response);
                return;
            }

            // Vérification de l'authentification
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Aucune authentification trouvée dans le contexte");
                filterChain.doFilter(request, response);
                return;
            }

            // Récupération de l'utilisateur
            String username = authentication.getName();
            log.debug("Utilisateur authentifié: {}", username);

            Utilisateur user = utilisateurRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("Utilisateur non trouvé en base: {}", username);
                        return new UsernameNotFoundException("Utilisateur non trouvé");
                    });

            // Vérification de l'entreprise
            if (user.getEntreprise() == null) {
                log.error("Aucune entreprise associée à l'utilisateur: {}", username);
                throw new DisabledException("Utilisateur non associé à une entreprise");
            }

            log.debug("Entreprise trouvée - ID: {}, Nom: {}, Active: {}",
                    user.getEntreprise().getId(),
                    user.getEntreprise().getNom(),
                    user.getEntreprise().isActive());

            // Définition du contexte tenant
            TenantContext.setCurrentTenant(user.getEntreprise().getId());
            TenantContext.setCurrentUser(username);
            log.info("Contexte tenant défini - Entreprise ID: {}", user.getEntreprise().getId());

        } catch (Exception e) {
            log.error("Erreur dans TenantFilter", e);
            handleTenantException(response, e);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            log.debug("Contexte tenant nettoyé");
        }
    }

    private boolean shouldSkipTenantCheck(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/v1/auth")
                || request.getServletPath().startsWith("/swagger")
                || request.getServletPath().startsWith("/v3/api-docs");
    }

    private void handleTenantException(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(
                String.format("{\"error\": \"%s\", \"message\": \"%s\"}",
                        "TENANT_ERROR",
                        e.getMessage())
        );
    }
}