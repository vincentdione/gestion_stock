package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.models.LigneVente;

import java.util.List;

public interface LigneVenteService {

    public List<LigneVenteDto> getAllLigneVente();
    public void deleteLigneVente(Long id);

    public LigneVenteDto getLigneVenteById(Long id);

    public LigneVenteDto createLigneVente(LigneVenteDto request);
}
