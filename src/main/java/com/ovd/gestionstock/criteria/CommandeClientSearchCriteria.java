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
public class CommandeClientSearchCriteria {
    private String code;
    private String nomClient;
    private String emailClient;
    private String numTelClient;
    private Date dateFrom;
    private Date dateTo;
    private String etat;
    private String modePayement;
}