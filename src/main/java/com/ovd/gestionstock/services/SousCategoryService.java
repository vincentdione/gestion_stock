package com.ovd.gestionstock.services;


import com.ovd.gestionstock.dto.SousCategoryDto;

import java.util.List;

public interface SousCategoryService {

    public List<SousCategoryDto> getAllSousCategory();
    public void deleteSousCategory(Long id);

    public SousCategoryDto getCategoryById(Long id);

    public SousCategoryDto createSousCategory(SousCategoryDto sousCategoryDto);

}
