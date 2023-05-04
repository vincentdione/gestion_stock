package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.CategoryDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleValidator {

    public static List<String> validate(ArticleDto articleDto){
        List<String> errors = new ArrayList<>();

        if(articleDto == null || !StringUtils.hasLength(articleDto.getCodeArticle())){
            errors.add("Veuillez renseigner le code de l'article");
        }

        if(!StringUtils.hasLength(articleDto.getDesignation())){
            errors.add("Veuillez renseigner la désignation de l'article");
        }

        if(articleDto.getPrixUnitaireHt() == null){
            errors.add("Veuillez renseigner le prix unitaire Ht de l'article");
        }

        if(articleDto.getPrixUnitaireTtc() == null){
                errors.add("Veuillez renseigner le prix unitaire Ttc de l'article");
        }

        if(articleDto.getTauxTval() == null){
            errors.add("Veuillez renseigner le taux Tva de l'article");
        }


        if(articleDto.getCategoryDto() == null){
            errors.add("Veuillez renseigner la catégorie de l'article");
        }

        return errors;
    }
}
