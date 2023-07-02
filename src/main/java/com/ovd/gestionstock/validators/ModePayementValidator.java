package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ModePayementDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ModePayementValidator {

    public static List<String> validate(ModePayementDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || !StringUtils.hasLength(dto.getCode())){
            errors.add("Veuillez renseigner le code du mode de payement");
        }
        if (!StringUtils.hasLength(dto.getDesignation())){
            errors.add("Veuillez renseigner la désignation du code de payement");
        }
        return errors;
    }

}
