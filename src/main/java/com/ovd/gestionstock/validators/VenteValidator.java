package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.dto.VenteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VenteValidator {

    public static List<String> validate(VenteDto venteDto){
        List<String> errors = new ArrayList<>();

        if(venteDto == null || !StringUtils.hasLength(venteDto.getCode())){
            errors.add("Veuillez renseigner le code");
        }
        return errors;
    }
}
