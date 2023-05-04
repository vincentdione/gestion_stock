package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.models.MvtStk;

import java.util.List;

public interface MvtStkService {

    public List<MvtStkDto> getAllMvtStk();
    public void deleteMvtStk(Long id);

    public MvtStkDto getMvtStkById(Long id);

    public MvtStkDto createMvtStk(MvtStkDto request);
}
