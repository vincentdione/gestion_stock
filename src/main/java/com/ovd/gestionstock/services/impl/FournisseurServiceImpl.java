package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.FournisseurDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Fournisseur;
import com.ovd.gestionstock.repositories.FournisseurRepository;
import com.ovd.gestionstock.services.FournisseurService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.FournisseurValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;

    @Override
    public List<FournisseurDto> getAllFournisseur() {
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<Fournisseur> fournisseurs = fournisseurRepository.findAllByIdEntreprise(currentTenant);

        return fournisseurs.stream()
                .map(FournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFournisseur(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("ID fournisseur est null", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur trouvé avec l'ID " + id,
                        ErrorCodes.FOURNISSEUR_NOT_FOUND
                ));

        try {
            tenantSecurityService.validateAccessToResource(fournisseur.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        fournisseurRepository.delete(fournisseur);
    }

    @Override
    public FournisseurDto getFournisseurById(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("ID fournisseur est null", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur trouvé avec l'ID " + id,
                        ErrorCodes.FOURNISSEUR_NOT_FOUND
                ));

        try {
            tenantSecurityService.validateAccessToResource(fournisseur.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        return FournisseurDto.fromEntity(fournisseur);
    }

    @Override
    public FournisseurDto createFournisseur(FournisseurDto request) {
        List<String> errors = FournisseurValidator.validate(request);

        if (!errors.isEmpty()) {
            log.error("Erreur de validation du fournisseur : {}", errors);
            throw new InvalidEntityException("Fournisseur invalide", ErrorCodes.FOURNISSEUR_NOT_FOUND, errors);
        }

        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        Fournisseur fournisseur = FournisseurDto.toEntity(request);
        fournisseur.setIdEntreprise(currentTenant);

        Fournisseur saved = fournisseurRepository.save(fournisseur);

        return FournisseurDto.fromEntity(saved);
    }
}
