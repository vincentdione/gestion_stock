package com.ovd.gestionstock.services;

import com.ovd.gestionstock.criteria.FournisseurSearchCriteria;
import com.ovd.gestionstock.dto.FournisseurDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FournisseurService {

    public List<FournisseurDto> getAllFournisseur();
    public void deleteFournisseur(Long id);

    public FournisseurDto getFournisseurById(Long id);

    public FournisseurDto createFournisseur(FournisseurDto request);
    // Méthodes de recherche avancée
    List<FournisseurDto> searchFournisseurs(FournisseurSearchCriteria criteria);
    Page<FournisseurDto> searchFournisseursPage(FournisseurSearchCriteria criteria, Pageable pageable);
    List<FournisseurDto> searchFournisseursByText(String searchText);
}
