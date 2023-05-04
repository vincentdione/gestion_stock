package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.models.Category;
import com.ovd.gestionstock.repositories.CategoryRepository;
import com.ovd.gestionstock.services.CategoryService;
import com.ovd.gestionstock.validators.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl  implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<CategoryDto> getAllCategory() {
        log.info("LISTE DES CATEGORIES +++++++++++++++++++++++ ++++++++++++++++++");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        if (id == null){
            log.error("Id categroy n'existe pas");
            return;
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        if (id == null) {
            log.error("ID n'existe pqs");
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
