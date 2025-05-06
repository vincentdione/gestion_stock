package com.ovd.gestionstock.services;

import com.ovd.gestionstock.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantSecurityService {
    private final TenantContext tenantContext;

    public void validateAccessToResource(Long resourceCompanyId) throws AccessDeniedException {
        Long currentTenant = tenantContext.getCurrentTenant();

        if (currentTenant == null) {
            log.error("Aucun tenant défini dans le contexte");
            throw new AccessDeniedException("Contexte tenant non disponible");
        }

        if (!resourceCompanyId.equals(currentTenant)) {
            log.warn("Tentative d'accès non autorisée. Tenant attendu: {}, Tenant actuel: {}",
                    resourceCompanyId, currentTenant);
            throw new AccessDeniedException("Accès refusé à cette ressource");
        }
    }
}