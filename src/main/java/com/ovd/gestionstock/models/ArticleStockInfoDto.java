package com.ovd.gestionstock.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleStockInfoDto {

    private Long id;
    private String name;
    private Map<String, BigDecimal> totalStockByUnit;
    private Map<String, BigDecimal> remainingStockByUnit;
}
