package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.models.Ventes;

import java.util.List;

public interface VenteService {

    public List<VenteDto> getAllVentes();
    public void deleteVentes(Long id);

    public VenteDto getVentesById(Long id);

    public VenteDto createVentes(VenteDto request);
}
