package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.MvtStkDto;

import java.util.ArrayList;
import java.util.List;

public class MvtStkValidator {

    public static List<String> validate(MvtStkDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || dto.getQuantite() == null){
            errors.add("Veuillez renseigner le code de la catégorie");
        }
        return errors;
    }
}
