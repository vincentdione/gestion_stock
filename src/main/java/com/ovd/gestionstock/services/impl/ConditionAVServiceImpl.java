package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.ConditionAVDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.ConditionAV;
import com.ovd.gestionstock.repositories.ConditionAVRepository;
import com.ovd.gestionstock.repositories.UniteRepository;
import com.ovd.gestionstock.services.ConditionAVService;
import com.ovd.gestionstock.validators.ConditionAValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConditionAVServiceImpl implements ConditionAVService {

    private final ConditionAVRepository conditionAVRepository;
    private final UniteRepository uniteRepository;
    private final TenantContext tenantContext;

    @Override
    public List<ConditionAVDto> getAllConditionAV() {
        Long tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new InvalidEntityException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        return conditionAVRepository.findAll().stream()
                .filter(conditionAV -> tenantId.equals(conditionAV.getIdEntreprise()))
                .map(ConditionAVDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConditionAVDto> getAllConditionAVWithDistinct() {
        Long tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new InvalidEntityException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        List<ConditionAVDto> allConditionAVs = conditionAVRepository.findAll().stream()
                .filter(conditionAV -> tenantId.equals(conditionAV.getIdEntreprise()))
                .map(ConditionAVDto::fromEntity)
                .collect(Collectors.toList());

        Map<Long, ConditionAVDto> uniqueArticlesMap = new LinkedHashMap<>();
        for (ConditionAVDto dto : allConditionAVs) {
            Long articleId = dto.getArticle().getId();
            uniqueArticlesMap.putIfAbsent(articleId, dto);
        }

        return new ArrayList<>(uniqueArticlesMap.values());
    }

    @Override
    public void deleteConditionAV(Long id) {
        if (id == null) {
            log.error("ID ConditionAV est null");
            return;
        }

        Optional<ConditionAV> conditionAVOptional = conditionAVRepository.findById(id);
        if (conditionAVOptional.isEmpty()) {
            log.warn("ConditionAV avec l'id {} introuvable", id);
            return;
        }

        ConditionAV conditionAV = conditionAVOptional.get();
        Long tenantId = tenantContext.getCurrentTenant();
        if (!tenantId.equals(conditionAV.getIdEntreprise())) {
            log.error("Tentative d'accès non autorisée au tenant {}", tenantId);
            return;
        }

        conditionAVRepository.deleteById(id);
        log.info("ConditionAV avec l'id {} supprimée", id);
    }

    @Override
    public ConditionAVDto getConditionAVById(Long id) {
        if (id == null) {
            log.error("ID ConditionAV est null");
            return null;
        }

        Optional<ConditionAV> conditionAVOptional = conditionAVRepository.findById(id);
        if (conditionAVOptional.isEmpty()) {
            return null;
        }

        ConditionAV conditionAV = conditionAVOptional.get();
        Long tenantId = tenantContext.getCurrentTenant();
        if (!tenantId.equals(conditionAV.getIdEntreprise())) {
            log.warn("Accès interdit à la conditionAV {}", id);
            return null;
        }

        return ConditionAVDto.fromEntity(conditionAV);
    }

    @Override
    public List<ConditionAVDto> findAllByIdArticle(Long idArticle) {
        Long tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new InvalidEntityException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        return conditionAVRepository.findByArticleId(idArticle).stream()
                .filter(conditionAV -> tenantId.equals(conditionAV.getIdEntreprise()))
                .map(ConditionAVDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ConditionAVDto createConditionAV(ConditionAVDto dto) {
        List<String> errors = ConditionAValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Veuillez renseigner les champs obligatoires");
            throw new InvalidEntityException("Veuillez renseigner les champs obligatoires", ErrorCodes.CONDITIONAV_NOT_VALID);
        }

        Long tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new InvalidEntityException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        ConditionAV conditionAV = ConditionAVDto.toEntity(dto);
        conditionAV.setIdEntreprise(tenantId);  // Associer au tenant courant

        ConditionAV saved = conditionAVRepository.save(conditionAV);
        log.info("ConditionAV créée avec succès pour le tenant {}", tenantId);

        return ConditionAVDto.fromEntity(saved);
    }
}
