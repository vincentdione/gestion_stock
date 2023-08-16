package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ConditionAVDto;
import com.ovd.gestionstock.dto.UniteDto;


import java.util.List;

public interface ConditionAVService {

    List<ConditionAVDto> getAllConditionAV();
    List<ConditionAVDto> getAllConditionAVWithDistinct();
     void deleteConditionAV(Long id);

     ConditionAVDto getConditionAVById(Long id);

    List<ConditionAVDto> findAllByIdArticle (Long idArticle);

     ConditionAVDto createConditionAV(ConditionAVDto unite);
}
