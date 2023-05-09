package com.ovd.gestionstock.exceptions;

public enum ErrorCodes {

    UTILISATEUR_NOT_FOUND(1000),
    ROLE_NOT_FOUND(2000),
    ARTICLE_NOT_FOUND(3000),
    CATEGORY_NOT_FOUND(3000),
    CLIENT_NOT_FOUND(4000),
    ENTREPRISE_NOT_FOUND(5000),
    VENTE_NOT_FOUND(6000),
    COMMANDE_CLIENT_NOT_FOUND(7000),
    COMMANDE_CLIENT_NON_MODIFIABLE(7001),
    FOURNISSEUR_NOT_FOUND(8000);

    private int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
