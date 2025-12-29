package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.EntrepriseDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {

    public static List<String> validate(EntrepriseDto dto){
        List<String> errors = new ArrayList<>();
        System.out.println(dto);
        if(dto == null) {
            errors.add("L'entreprise ne peut pas être null");
            return errors;
        }

        // Validation des champs obligatoires de l'entreprise
        if(!StringUtils.hasLength(dto.getNom())){
            errors.add("Veuillez renseigner le nom de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getCodeFiscal())){
            errors.add("Veuillez renseigner le code fiscal de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getEmail())){
            errors.add("Veuillez renseigner l'email de l'entreprise");
        }

        if(!StringUtils.hasLength(dto.getNumTel())){
            errors.add("Veuillez renseigner le téléphone de l'entreprise");
        }

        // Validation de l'adresse (optionnelle selon vos besoins)
        if(dto.getAdresseDto() != null) {
            if(!StringUtils.hasLength(dto.getAdresseDto().getVille())){
                errors.add("Veuillez renseigner la ville de l'entreprise");
            }
            if(!StringUtils.hasLength(dto.getAdresseDto().getPays())){
                errors.add("Veuillez renseigner le pays de l'entreprise");
            }
            if(!StringUtils.hasLength(dto.getAdresseDto().getCodePostal())){
                errors.add("Veuillez renseigner le code postal de l'entreprise");
            }
        }
        // Si l'adresse est obligatoire, décommentez cette partie :
        /*
        else {
            errors.add("Veuillez renseigner l'adresse de l'entreprise");
        }
        */

        return errors;
    }
}