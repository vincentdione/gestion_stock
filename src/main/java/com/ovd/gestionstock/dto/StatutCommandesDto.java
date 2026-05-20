package com.ovd.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatutCommandesDto {
    private Long total;
    private Long enPreparation;
    private Long validee;
    private Long livree;
    private Long annulee;
}