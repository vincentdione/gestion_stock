package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Ventes;
import com.ovd.gestionstock.repositories.VenteRepository;
import com.ovd.gestionstock.services.VenteService;
import com.ovd.gestionstock.validators.UtilisateurValidator;
import com.ovd.gestionstock.validators.VenteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;

    @Override
    public List<VenteDto> getAllVentes() {
        return venteRepository.findAll().stream().map(VenteDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteVentes(Long id) {
        if (id == null){
            log.error("ID est null");
            return;
        }

        venteRepository.deleteById(id);
    }

    @Override
    public VenteDto getVentesById(Long id) {

        if (id == null){
            log.error("ID est null");
            return null;
        }

        Optional<Ventes> ventes = venteRepository.findById(id);


        return VenteDto.fromEntity(ventes.get());
    }

    @Override
    public VenteDto createVentes(VenteDto request) {
        List<String> errors = VenteValidator.validate(request);

        if (!errors.isEmpty()) {
            log.error("Erreur verifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obligatoires", ErrorCodes.VENTE_NOT_FOUND);
        }
        Ventes ventes = venteRepository.save(VenteDto.toEntity(request));

        return VenteDto.fromEntity(ventes);
    }
}
