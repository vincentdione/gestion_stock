package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.dto.FournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurValidator {

    public static List<String> validate(FournisseurDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || !StringUtils.hasLength(dto.getNom())){
            errors.add("Veuillez renseigner le nom du fournisseur");
        }

        if(!StringUtils.hasLength(dto.getPrenom())){
            errors.add("Veuillez renseigner le prenom du fournisseur");
        }

        if(!StringUtils.hasLength(dto.getEmail())){
            errors.add("Veuillez renseigner l'email du fournisseur");
        }

        if(!StringUtils.hasLength(dto.getNumTel())){
            errors.add("Veuillez renseigner le tel du fournisseur");
        }

        return errors;
    }
}
