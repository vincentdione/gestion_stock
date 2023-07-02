package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.UniteDto;
import com.ovd.gestionstock.exceptions.ErrorCodes;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Unite;
import com.ovd.gestionstock.repositories.UniteRepository;
import com.ovd.gestionstock.services.UniteService;
import com.ovd.gestionstock.validators.UniteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniteServiceImpl implements UniteService {

    private final UniteRepository uniteRepository;

    @Override
    public List<UniteDto> getAllUnite() {
        return uniteRepository.findAll().stream().map(UniteDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteUnite(Long id) {
        if (id == null){
           log.error("ID IS NULL");
        }

        uniteRepository.deleteById(id);

    }

    @Override
    public UniteDto getUniteById(Long id) {
        if (id == null){
            log.error("ID IS NULL");
            return  null;
        }

        Optional<Unite> unite = uniteRepository.findById(id);
        UniteDto uniteDto = UniteDto.fromEntity(unite.get()) ;
        return uniteDto ;
    }

    @Override
    public List<UniteDto> findAllByIdArticle(Long idArticle) {

        return null;
    }

    @Override
    public UniteDto createUnite(UniteDto unite) {
        if (unite == null){
            log.error(" UNITE IS NULL");
            return  null;
        }

        List<String> errors = UniteValidator.validate(unite);

        if (!errors.isEmpty()){
            log.info("Errors "+errors);
            throw  new InvalidEntityException("L'enregistrement de l'unité est invalide", ErrorCodes.ROLE_NOT_FOUND);
        }

        return UniteDto.fromEntity(uniteRepository.save(UniteDto.toEntity(unite)));
    }
}
