package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.FournisseurDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Fournisseur;
import com.ovd.gestionstock.repositories.FournisseurRepository;
import com.ovd.gestionstock.services.FournisseurService;
import com.ovd.gestionstock.validators.EntrepriseValidator;
import com.ovd.gestionstock.validators.FournisseurValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FournisseurServiceImpl implements FournisseurService{

    private final FournisseurRepository fournisseurRepository;
    @Override
    public List<FournisseurDto> getAllFournisseur() {
        return fournisseurRepository.findAll().stream().map(FournisseurDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteFournisseur(Long id) {

        if (id == null){
            log.error("Id est null");
            return;
        }
        fournisseurRepository.deleteById(id);
    }

    @Override
    public FournisseurDto getFournisseurById(Long id) {
        if (id == null){
            log.error("Id est null");
            return null;
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        FournisseurDto fournisseurDto = FournisseurDto.fromEntity(fournisseur.get());

        return fournisseurDto;
    }

    @Override
    public FournisseurDto createFournisseur(FournisseurDto request) {
        List<String> errors = FournisseurValidator.validate(request);

        if (!errors.isEmpty()) {
            log.error("Erreur verifier les champs obligatoires");
            throw new InvalidEntityException("Saisissez les champs obliagtoires", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }
        FournisseurDto fournisseurDto = FournisseurDto.fromEntity(fournisseurRepository.save(FournisseurDto.toEntity(request)));


        return fournisseurDto;
    }
}
