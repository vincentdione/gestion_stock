package com.ovd.gestionstock.validators;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.CommandeClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeClientValidator {

    public static List<String> validate(CommandeClientDto commandeClientDto) {
        List<String> errors = new ArrayList<>();

        if (commandeClientDto == null) {
            errors.add("La commande ne peut pas être null");
            return errors;
        }

        if (commandeClientDto.getClientDto() == null || commandeClientDto.getClientDto().getId() == null) {
            errors.add("Veuillez renseigner le client de la commande");
        }

        if (commandeClientDto.getLigneCommandeClients() == null || commandeClientDto.getLigneCommandeClients().isEmpty()) {
            errors.add("Veuillez ajouter au moins une ligne de commande");
        }

        return errors;
    }

}
