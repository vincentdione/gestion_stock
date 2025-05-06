package com.ovd.gestionstock.config;

import org.springframework.stereotype.Component;


@Component
public class TenantContext {
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

    public Long getCurrentTenant() {  // Remove static
        return CURRENT_TENANT.get();
    }

    public void setCurrentTenant(Long tenantId) {  // Remove static
        CURRENT_TENANT.set(tenantId);
    }

    public String getCurrentUser() {  // Remove static
        return CURRENT_USER.get();
    }

    public void setCurrentUser(String username) {  // Remove static
        CURRENT_USER.set(username);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_USER.remove();
    }
}