package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.config.TenantContext;
import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.models.Category;
import com.ovd.gestionstock.models.SousCategory;
import com.ovd.gestionstock.repositories.CategoryRepository;
import com.ovd.gestionstock.services.CategoryService;
import com.ovd.gestionstock.services.TenantSecurityService;
import com.ovd.gestionstock.validators.CategoryValidator;
import com.ovd.gestionstock.validators.SousCategoryValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TenantSecurityService tenantSecurityService;
    private final TenantContext tenantContext;

    @Override
    public List<CategoryDto> getAllCategory() {
        if (tenantContext.getCurrentTenant() == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            log.warn("Aucune catégorie trouvée pour le tenant {}", tenantContext.getCurrentTenant());
        }

        return categories.stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("ID ne peut pas être null", ErrorCodes.CATEGORY_NOT_FOUND);
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Catégorie non trouvée avec l'id: " + id,
                        ErrorCodes.CATEGORY_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(category.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("ID ne peut pas être null", ErrorCodes.CATEGORY_NOT_FOUND);
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Catégorie non trouvée avec l'id: " + id,
                        ErrorCodes.CATEGORY_NOT_FOUND));

        try {
            tenantSecurityService.validateAccessToResource(category.getIdEntreprise());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        return CategoryDto.fromEntity(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        List<String> errors = CategoryValidator.validate(categoryDto);
        if (!errors.isEmpty()) {
            throw new EntityNotFoundException(
                    "Données de catégorie invalides: " + String.join(", ", errors),
                    ErrorCodes.CATEGORY_NOT_FOUND);
        }

        Long currentTenant = tenantContext.getCurrentTenant();
        if (currentTenant == null) {
            throw new IllegalStateException("Aucun tenant défini dans le contexte");
        }

        Category category = CategoryDto.toEntity(categoryDto);
        category.setIdEntreprise(currentTenant);

        Category savedCategory = categoryRepository.save(category);
        return CategoryDto.fromEntity(savedCategory);
    }
}