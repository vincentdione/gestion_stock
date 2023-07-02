package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.ConditionAVDto;
import com.ovd.gestionstock.dto.UniteDto;
import com.ovd.gestionstock.exceptions.InvalidEntityException;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.ConditionAV;
import com.ovd.gestionstock.repositories.ConditionAVRepository;
import com.ovd.gestionstock.services.ConditionAVService;
import com.ovd.gestionstock.validators.ConditionAValidator;
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
public class ConditionAVServiceImpl implements ConditionAVService {

    private final ConditionAVRepository conditionAVRepository;

    @Override
    public List<ConditionAVDto> getAllConditionAV() {
        return conditionAVRepository.findAll().stream().map(ConditionAVDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteConditionAV(Long id) {
        if (id == null){
            log.error("ID ConditionAV est null");
            return;
        }

        conditionAVRepository.deleteById(id);
    }

    @Override
    public ConditionAVDto getConditionAVById(Long id) {
        if (id == null){
            log.error("ID ConditionAV est null");
            return null;
        }

        Optional<ConditionAV> conditionAV = conditionAVRepository.findById(id);

        return ConditionAVDto.fromEntity(conditionAV.get());
    }

    @Override
    public List<ConditionAVDto> findAllByIdArticle(Long idArticle) {
        return null;
    }

    @Override
    public ConditionAVDto createConditionAV(ConditionAVDto dto) {
        List<String> errors = ConditionAValidator.validate(dto);

        if (!errors.isEmpty()){
            log.error("Veuillez renseigner les champs obligatoires");
            throw  new InvalidEntityException("\"Veuillez renseigner les champs obligatoires\"");
        }

        System.out.println(" ------- createConditionAV ---------------"+dto);

        ArticleDto articleDto = dto.getArticle();
        Article article = ArticleDto.toEntity(articleDto);
        System.out.println("Article ===== " + article);

        if (article.getConditions() == null) {
            article.setConditions(new ArrayList<>()); // Initialisation de la liste conditions
        }

        article.getConditions().add(ConditionAVDto.toEntity(dto));

        return ConditionAVDto.fromEntity(conditionAVRepository.save(ConditionAVDto.toEntity(dto)));
    }
}
