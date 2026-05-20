package com.ovd.gestionstock.services;

import com.ovd.gestionstock.criteria.VenteSearchCriteria;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.models.Ventes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    VenteDto updateVente(Long id, VenteDto venteDto);

    VenteDto updateVenteClient(Long id, VenteDto venteDto);

    List<VenteDto> get10LatestVentes();

    // Nouvelles méthodes de recherche
    Page<VenteDto> searchVentes(VenteSearchCriteria criteria);
    Page<VenteDto> searchVentes(String nomClient, String prenomClient, String codeVente, Pageable pageable);
    List<VenteDto> searchVentesByNomClient(String nomClient);


}
