package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.models.Category;
import com.ovd.gestionstock.models.SousCategory;
import com.ovd.gestionstock.repositories.CategoryRepository;
import com.ovd.gestionstock.services.CategoryService;
import com.ovd.gestionstock.validators.CategoryValidator;
import com.ovd.gestionstock.validators.SousCategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategory() {
        log.info("LISTE DES CATEGORIES +++++++++++++++++++++++ ++++++++++++++++++");
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()){
            log.error("Liste des catégories est vide !!! ");
        }
        return categories.stream().map(CategoryDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (id == null){
            log.error("Id Souscategroy n'existe pas");
            return;
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        if (id == null) {
            log.error("ID n'existe pas");
            return null;
        }
        Optional<Category> category = categoryRepository.findById(id);
        CategoryDto categoryDto = CategoryDto.fromEntity(category.get());

        log.info("category.get() " + categoryDto);

        return  Optional.of(categoryDto).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec l'id  = " +id+ " n'a été trouvé",
                        ErrorCodes.CATEGORY_NOT_FOUND
                )
        );
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        List<String> errors = CategoryValidator.validate(categoryDto);

        if(!errors.isEmpty()){
            log.error("Error " +errors);
            throw new EntityNotFoundException("Les informations ne sont pas correctes" , ErrorCodes.CATEGORY_NOT_FOUND);
        }

        Category category = categoryRepository.save(CategoryDto.toEntity(categoryDto));

        return CategoryDto.fromEntity(category);
    }
}
