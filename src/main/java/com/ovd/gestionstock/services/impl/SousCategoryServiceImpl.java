package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.SousCategoryDto;
import com.ovd.gestionstock.exceptions.EntityNotFoundException;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.models.SousCategory;
import com.ovd.gestionstock.repositories.SousCategoryRepository;
import com.ovd.gestionstock.services.SousCategoryService;
import com.ovd.gestionstock.validators.SousCategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SousCategoryServiceImpl implements SousCategoryService {

    private final SousCategoryRepository sousCategoryRepository;
    @Override
    public List<SousCategoryDto> getAllSousCategory() {
        log.info("LISTE DES CATEGORIES +++++++++++++++++++++++ ++++++++++++++++++");
        List<SousCategory> categories = sousCategoryRepository.findAll();
        return categories.stream().map(SousCategoryDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteSousCategory(Long id) {
        if (id == null){
            log.error("Id Souscategroy n'existe pas");
            return;
        }
        sousCategoryRepository.deleteById(id);
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
        List<String> errors = SousCategoryValidator.validate(sousCategoryDto);

        if(!errors.isEmpty()){
            log.error("Error " +errors);
            throw new EntityNotFoundException("Les informations ne sont pas correctes" , ErrorCodes.CATEGORY_NOT_FOUND);
        }
        log.error("-----------------------------------");
        log.error("---------------sousCategoryDto--------------------"+sousCategoryDto);
        log.error("-----------------------------------");
        SousCategory sousCategory = sousCategoryRepository.save(SousCategoryDto.toEntity(sousCategoryDto));

        return SousCategoryDto.fromEntity(sousCategory);
    }
}
