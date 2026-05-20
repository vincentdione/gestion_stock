package com.ovd.gestionstock.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeFournisseurSearchCriteria {
    private String code;
    private String nomFournisseur;
    private String emailFournisseur;
    private String numTelFournisseur;
    private Date dateFrom;
    private Date dateTo;
    private String etat;
    private Long modePayementId;
    private String modePayementCode;
    private String modePayementDesignation;
}