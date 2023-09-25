package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.models.Ventes;

import java.math.BigDecimal;
import java.util.List;

public interface VenteService {

    public List<VenteDto> getAllVentes();
    public void deleteVentes(Long id);

    public VenteDto getVentesById(Long id);

    List<LigneVenteDto> findAllLigneVentesByVenteId(Long id);

    VenteDto findByCode(String code);

    public VenteDto createVentes(VenteDto request);

    BigDecimal getMontantTotalVentes(List<Ventes> ventes);


}
