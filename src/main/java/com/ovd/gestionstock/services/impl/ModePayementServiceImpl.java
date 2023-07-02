package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ModePayementDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.ModePayement;
import com.ovd.gestionstock.repositories.ModePayementRepository;
import com.ovd.gestionstock.services.ModePayementService;
import com.ovd.gestionstock.validators.ModePayementValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModePayementServiceImpl implements ModePayementService {

    private final ModePayementRepository modePayementRepository;
    @Override
    public List<ModePayementDto> getAllModePayement() {
        return modePayementRepository.findAll().stream().map(ModePayementDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteModePayement(Long id) {
        if (id == null){
            log.error("Id Mode Payement est null");
            return;
        }

        modePayementRepository.deleteById(id);

    }

    @Override
    public ModePayementDto getModePayementById(Long id) {
        if (id == null){
            log.error("Id Mode Payement est null");
            return  null;
        }
        Optional<ModePayement> findMode = modePayementRepository.findById(id);

        return ModePayementDto.fromEntity(findMode.get());
    }

    @Override
    public ModePayementDto createModePayement(ModePayementDto mode) {

        List<String> errors = ModePayementValidator.validate(mode);

        if (!errors.isEmpty()){
            log.error("Erreur verifier les champs saisis");
            throw  new InvalidEntityException("Veuillez vérifier si les champs obligatoires sont saisis", ErrorCodes.MODE_PAYEMENT_NOT_FOUND);
        }


        return ModePayementDto.fromEntity(modePayementRepository.save(ModePayementDto.toEntity(mode)));
    }
}
