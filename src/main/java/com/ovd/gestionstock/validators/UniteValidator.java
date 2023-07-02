package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.UniteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UniteValidator {

    public static List<String> validate(UniteDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || !StringUtils.hasLength(dto.getNom())){
            errors.add("Veuillez renseigner le nom de l'unité");
        }
        if (!StringUtils.hasLength(dto.getDesignation())){
            errors.add("Veuillez renseigner la désignation de l'unité");
        }
        return errors;
    }

}
