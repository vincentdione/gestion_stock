package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.ClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    public static List<String> validate(ClientDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || !StringUtils.hasLength(dto.getNom())){
            errors.add("Veuillez renseigner le nom du client");
        }

        if(!StringUtils.hasLength(dto.getPrenom())){
            errors.add("Veuillez renseigner le prenom du client");
        }

        if(!StringUtils.hasLength(dto.getEmail())){
            errors.add("Veuillez renseigner l'email du client");
        }

        if(!StringUtils.hasLength(dto.getNumTel())){
            errors.add("Veuillez renseigner le tel du client");
        }

        return errors;
    }
}
