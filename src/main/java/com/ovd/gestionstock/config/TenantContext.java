package com.ovd.gestionstock.config;

public class TenantContext {
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

    public static Long getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void setCurrentUser(String username) {
        CURRENT_USER.set(username);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_USER.remove();
    }
}