package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ClientDto;
import com.ovd.gestionstock.dto.EntrepriseDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {

    public static List<String> validate(EntrepriseDto dto){
        List<String> errors = new ArrayList<>();

        if(dto == null || !StringUtils.hasLength(dto.getNom())){
            errors.add("Veuillez renseigner le nom de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getAdresseDto().getVille())){
            errors.add("Veuillez renseigner la ville de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getCodeFiscal())){
            errors.add("Veuillez renseigner le code fiscal de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getEmail())){
            errors.add("Veuillez renseigner l'email de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getNumTel())){
            errors.add("Veuillez renseigner le tel de l'entreprise");
        }

        return errors;
    }
}
