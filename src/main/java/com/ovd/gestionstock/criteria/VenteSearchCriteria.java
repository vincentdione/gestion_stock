package com.ovd.gestionstock.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteSearchCriteria {

    private String nomClient;
    private String prenomClient;
    private String codeVente;
    private String numeroClient;
    private String adresse;
    private Date dateFrom;
    private Date dateTo;

    // Pagination
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    @Builder.Default
    private String sortBy = "dateVente";

    @Builder.Default
    private Sort.Direction sortDirection = Sort.Direction.DESC;

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}