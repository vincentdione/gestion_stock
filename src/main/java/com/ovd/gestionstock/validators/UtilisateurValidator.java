package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurValidator {

    public static List<String> validate(UtilisateurDto utilitiesateurDto){
        List<String> errors = new ArrayList<>();

        if(utilitiesateurDto == null || !StringUtils.hasLength(utilitiesateurDto.getEmail())){
            errors.add("Veuillez renseigner l'email de l'utilisateur");
        }
        return errors;
    }
}
