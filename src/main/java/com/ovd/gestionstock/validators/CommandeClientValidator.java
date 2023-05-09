package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.CommandeClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeClientValidator {

    public static List<String> validate(CommandeClientDto commandeClientDto){
        List<String> errors = new ArrayList<>();

        if(commandeClientDto == null || !StringUtils.hasLength(commandeClientDto.getCode())){
            errors.add("Veuillez renseigner le code de l'article");
        }


        return errors;
    }
}
