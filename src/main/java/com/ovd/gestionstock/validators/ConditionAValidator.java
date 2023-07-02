package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ConditionAVDto;
import com.ovd.gestionstock.dto.UniteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConditionAValidator {

    public static List<String> validate(ConditionAVDto dto){
        List<String> errors = new ArrayList<>();
//
//        if(dto == null || dto.getQuantity() == null){
//            errors.add("Veuillez renseigner le nom de l'unité");
//        }

        if(dto == null || Double.valueOf(dto.getPrice()) == null){
            errors.add("Veuillez renseigner le nom de l'unité");
        }

        return errors;
    }

}
