package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.exceptions.InvalidOperationException;
import com.ovd.gestionstock.models.Category;
import com.ovd.gestionstock.models.SousCategory;
import com.ovd.gestionstock.repositories.CategoryRepository;
import com.ovd.gestionstock.repositories.SousCategoryRepository;
import com.ovd.gestionstock.services.SousCategoryService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.SousCategoryValidator;
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
public class SousCategoryServiceImpl implements SousCategoryService {

    private final SousCategoryRepository sousCategoryRepository;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;
    private final CategoryRepository categoryRepository;

    @Override
    public List<SousCategoryDto> getAllSousCategory() {
        log.debug("Récupération de toutes les sous-catégories pour l'entreprise {}", tenantContext.getCurrentTenant());

        if (tenantContext.getCurrentTenant() == null) {
            throw new InvalidOperationException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        List<SousCategory> categories = sousCategoryRepository.findAll();

        if (categories.isEmpty()) {
            log.warn("Aucune sous-catégorie trouvée pour l'entreprise {}", tenantContext.getCurrentTenant());
        }

        return categories.stream()
                .map(SousCategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSousCategory(Long id) {
        log.debug("Suppression de la sous-catégorie {} pour l'entreprise {}", id, tenantContext.getCurrentTenant());

        if (id == null) {
            log.error("ID de sous-catégorie ne peut pas être null");
            throw new EntityNotFoundException("ID ne peut pas être null", ErrorCodes.SOUS_CATEGORY_NOT_FOUND);
        }

        SousCategory sousCategory = sousCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Sous-catégorie avec ID {} non trouvée", id);
                    return new EntityNotFoundException(
                            "Aucune sous-catégorie avec l'id = " + id + " n'a été trouvée",
                            ErrorCodes.SOUS_CATEGORY_NOT_FOUND);
                });
        try {
            tenantSecurityService.validateAccessToResource(sousCategory.getIdEntreprise());
        }
        catch (Exception e){
            log.error(e.getMessage());
        }

        sousCategoryRepository.delete(sousCategory);
        log.info("Sous-catégorie {} supprimée avec succès", id);
    }

    @Override
    public SousCategoryDto getCategoryById(Long id) {
        if (id == null) {
            log.error("ID n'existe pqs");
            return null;
        }
        Optional<SousCategory> category = sousCategoryRepository.findById(id);
        SousCategoryDto sousCategoryDto = SousCategoryDto.fromEntity(category.get());

        log.info("category.get() " + sousCategoryDto);

        return  Optional.of(sousCategoryDto).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec l'id  = " +id+ " n'a été trouvé",
                            ErrorCodes.CATEGORY_NOT_FOUND
                )
        );
    }

    @Override
    public SousCategoryDto createSousCategory(SousCategoryDto sousCategoryDto) {
        log.debug("Création d'une nouvelle sous-catégorie pour l'entreprise {}", tenantContext.getCurrentTenant());

        // Validation du DTO
        List<String> errors = SousCategoryValidator.validate(sousCategoryDto);
        if (!errors.isEmpty()) {
            log.error("Données de sous-catégorie non valides : {}", errors);
            throw new EntityNotFoundException(
                    "Les informations ne sont pas correctes : " + String.join(", ", errors),
                    ErrorCodes.CATEGORY_NOT_FOUND);
        }

        // Vérification du contexte tenant
        if (tenantContext.getCurrentTenant() == null) {
            throw new InvalidOperationException("Aucun tenant défini dans le contexte", ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        // Conversion et sauvegarde
        SousCategory sousCategory = SousCategoryDto.toEntity(sousCategoryDto);
        sousCategory.setIdEntreprise(tenantContext.getCurrentTenant());

        SousCategory savedSousCategory = sousCategoryRepository.save(sousCategory);
        log.info("Sous-catégorie créée avec succès avec l'ID {}", savedSousCategory.getId());

        return SousCategoryDto.fromEntity(savedSousCategory);
    }

    @Override
    public SousCategoryDto getOrCreateSousCategory(String sousCategoryCode,
                                                   String sousCategoryName,
                                                   String categoryCode) {
        // 1. Validation des paramètres obligatoires
        if (sousCategoryCode == null || sousCategoryCode.isBlank()) {
            throw new InvalidEntityException("Le code de sous-catégorie est obligatoire",
                    ErrorCodes.SOUS_CATEGORY_CODE_REQUIRED);
        }

        // 2. Vérification du contexte tenant
        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new InvalidOperationException("Aucun tenant défini",
                    ErrorCodes.TENANT_CONTEXT_REQUIRED);
        }

        // 3. Vérifier d'abord l'existence de la catégorie parente
        Category parentCategory = categoryRepository.findByCodeAndIdEntreprise(categoryCode, currentTenant)
                .orElseThrow(() -> new EntityNotFoundException(
                        "La catégorie parente avec le code '" + categoryCode + "' n'existe pas",
                        ErrorCodes.CATEGORY_NOT_FOUND));

        // 4. Recherche de la sous-catégorie existante
        Optional<SousCategory> existingSousCategory = sousCategoryRepository
                .findByCodeAndIdEntreprise(sousCategoryCode, currentTenant);

        if (existingSousCategory.isPresent()) {
            // Vérifier la cohérence avec la catégorie parente
            if (!existingSousCategory.get().getCategory().getCode().equals(categoryCode)) {
                throw new InvalidOperationException(
                        "La sous-catégorie existe déjà avec une catégorie parente différente",
                        ErrorCodes.SOUS_CATEGORY_CONFLICT);
            }
            return SousCategoryDto.fromEntity(existingSousCategory.get());
        }

        // 5. Création de la nouvelle sous-catégorie
        SousCategoryDto newSousCategory = SousCategoryDto.builder()
                .code(sousCategoryCode)
                .designation(sousCategoryName != null ? sousCategoryName : "Sous-catégorie " + sousCategoryCode)
                .category(CategoryDto.fromEntity(parentCategory))
                .build();

        // Validation
        List<String> errors = SousCategoryValidator.validate(newSousCategory);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Sous-catégorie invalide",
                    ErrorCodes.SOUS_CATEGORY_NOT_VALID,
                    errors);
        }

        return createSousCategory(newSousCategory);
    }
}
