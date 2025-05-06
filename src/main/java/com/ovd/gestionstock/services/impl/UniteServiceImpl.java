package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.UniteDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.ConditionAV;
import com.ovd.gestionstock.models.Unite;
import com.ovd.gestionstock.repositories.ConditionAVRepository;
import com.ovd.gestionstock.repositories.UniteRepository;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.services.UniteService;
import com.ovd.gestionstock.validators.UniteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniteServiceImpl implements UniteService {

    private final UniteRepository uniteRepository;
    private final ConditionAVRepository conditionAVRepository;
    private final TenantContext tenantContext;
    private final TenantSecurityService tenantSecurityService;

    @Override
    public List<UniteDto> getAllUnite() {
        if (tenantContext.getCurrentTenant() == null) {
            throw new InvalidEntityException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        return uniteRepository.findAll().stream()
                .filter(unite -> tenantContext.getCurrentTenant().equals(unite.getIdEntreprise()))
                .map(UniteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUnite(Long id) {
        if (id == null) {
            log.error("ID IS NULL");
            throw new EntityNotFoundException("L'ID de l'unité ne peut pas être nul", ErrorCodes.UNITE_NOT_FOUND);
        }

        Unite unite = uniteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucune unité trouvée avec l'ID = " + id, ErrorCodes.UNITE_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(unite.getIdEntreprise());
        } catch (Exception e) {
            log.error("Accès refusé à l'unité {} : {}", id, e.getMessage());
            throw new InvalidEntityException("Accès refusé à cette ressource", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        uniteRepository.delete(unite);
        log.info("Unité {} supprimée avec succès", id);
    }

    @Override
    public UniteDto getUniteById(Long id) {
        if (id == null) {
            log.error("ID IS NULL");
            return null;
        }

        Unite unite = uniteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unité non trouvée", ErrorCodes.UNITE_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(unite.getIdEntreprise());
        } catch (Exception e) {
            log.error("Accès refusé à l'unité {} : {}", id, e.getMessage());
            throw new InvalidEntityException("Accès refusé à cette ressource", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        return UniteDto.fromEntity(unite);
    }

    @Override
    public List<UniteDto> findAllByIdArticle(Long idArticle) {
        List<Unite> unites = new ArrayList<>();
        List<ConditionAV> conditionAVs = conditionAVRepository.findByArticleId(idArticle);

        for (ConditionAV conditionAV : conditionAVs) {
            Unite unite = conditionAV.getUnite();
            if (unite != null && tenantContext.getCurrentTenant().equals(unite.getIdEntreprise())) {
                unites.add(unite);
            }
        }

        return unites.stream().map(UniteDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public UniteDto createUnite(UniteDto uniteDto) {
        if (uniteDto == null) {
            log.error("UNITE IS NULL");
            return null;
        }

        List<String> errors = UniteValidator.validate(uniteDto);
        if (!errors.isEmpty()) {
            log.info("Erreurs : {}", errors);
            throw new InvalidEntityException("L'enregistrement de l'unité est invalide", ErrorCodes.UNITE_NOT_VALID);
        }

        if (tenantContext.getCurrentTenant() == null) {
            throw new InvalidEntityException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        Unite unite = UniteDto.toEntity(uniteDto);
        unite.setIdEntreprise(tenantContext.getCurrentTenant());

        Unite savedUnite = uniteRepository.save(unite);
        log.info("Unité créée avec succès avec l'ID {}", savedUnite.getId());

        return UniteDto.fromEntity(savedUnite);
    }
}
