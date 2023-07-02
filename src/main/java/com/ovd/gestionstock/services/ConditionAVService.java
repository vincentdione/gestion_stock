package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ConditionAVDto;


import java.util.List;

public interface ConditionAVService {

     List<ConditionAVDto> getAllConditionAV();
     void deleteConditionAV(Long id);

     ConditionAVDto getConditionAVById(Long id);

    List<ConditionAVDto> findAllByIdArticle (Long idArticle);

     ConditionAVDto createConditionAV(ConditionAVDto unite);
}
