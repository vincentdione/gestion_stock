package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.CommandeClientDto;
import com.ovd.gestionstock.dto.CommandeFournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeFournisseurValidator {

    public static List<String> validate(CommandeFournisseurDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || !StringUtils.hasLength(dto.getCode())){
            errors.add("Veuillez renseigner le code de l'article");
        }


        return errors;
    }
}
