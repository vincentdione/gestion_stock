package com.ovd.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvolutionMensuelleDto {
    private String mois; // Format: "2024-01"
    private String libelleMois; // Format: "Janvier 2024"
    private BigDecimal chiffreAffaire;
    private Integer nombreVentes;
    private Integer nombreCommandes;
}