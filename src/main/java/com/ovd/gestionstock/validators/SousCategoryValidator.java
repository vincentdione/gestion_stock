package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.SousCategoryDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SousCategoryValidator {

    public static List<String> validate(SousCategoryDto sousCategoryDto){
        List<String> errors = new ArrayList<>();

        if(sousCategoryDto == null || !StringUtils.hasLength(sousCategoryDto.getCode())){
            errors.add("Veuillez renseigner le code de la catégorie");
        }
        return errors;
    }
}
