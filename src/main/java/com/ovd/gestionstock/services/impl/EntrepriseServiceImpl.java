package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Entreprise;
import com.ovd.gestionstock.repositories.EntrepriseRepository;
import com.ovd.gestionstock.services.EntrepriseService;
import com.ovd.gestionstock.validators.EntrepriseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    @Override
    public List<EntrepriseDto> getAllEntreprise() {
        return entrepriseRepository.findAll().stream().map(EntrepriseDto::fromEntity).collect(Collectors.toList());
    }


    @Override
    public void deleteEntreprise(Long id) {
        if (id == null){
            log.error("ID est null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }

    @Override
    public EntrepriseDto getEntrepriseById(Long id) {
        if (id == null){
            log.error("ID est null");
            return null;
        }
        Optional<Entreprise> entreprise = entrepriseRepository.findById(id);
        EntrepriseDto entrepriseDto = EntrepriseDto.fromEntity(entreprise.get());
        return entrepriseDto;
    }

    @Override
    public EntrepriseDto createEntreprise(EntrepriseDto request) {
        List<String> errors = EntrepriseValidator.validate(request);

        if (!errors.isEmpty()) {
            log.error("Erreur verifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obliagtoires", ErrorCodes.ENTREPRISE_NOT_FOUND);
        }

        Entreprise entreprise = entrepriseRepository.save(EntrepriseDto.toEntity(request));

        return EntrepriseDto.fromEntity(entreprise);
    }
}
