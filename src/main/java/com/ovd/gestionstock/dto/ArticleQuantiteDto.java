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
public class ArticleQuantiteDto {
    private Long id;
    private String code;
    private String nom;
    private BigDecimal quantiteVendue;
    private String unite;
}