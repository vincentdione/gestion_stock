package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ModePayementDto;
import com.ovd.gestionstock.dto.UniteDto;

import java.util.List;

public interface ModePayementService {

    public List<ModePayementDto> getAllModePayement();
    public void deleteModePayement(Long id);

    public ModePayementDto getModePayementById(Long id);

    public ModePayementDto createModePayement(ModePayementDto mode);
}
