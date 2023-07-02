package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.UniteDto;

import java.util.List;

public interface UniteService {

    public List<UniteDto> getAllUnite();
    public void deleteUnite(Long id);

    public UniteDto getUniteById(Long id);

    List<UniteDto> findAllByIdArticle (Long idArticle);

    public UniteDto createUnite(UniteDto unite);
}
