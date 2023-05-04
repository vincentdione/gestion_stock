package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.CommandeFournisseurController;
import com.ovd.gestionstock.dto.CommandeFournisseurDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommandeFournisseurApi implements CommandeFournisseurController {

    @Override
    public ResponseEntity<CommandeFournisseurDto> save(CommandeFournisseurDto request) {
        return null;
    }

    @Override
    public ResponseEntity<List<CommandeFournisseurDto>> getAllCommandeFournisseurs() {
        return null;
    }

    @Override
    public ResponseEntity<CommandeFournisseurDto> getCommandeFournisseurById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity deleteCommandeFournisseur(Long id) {
        return null;
    }
}
