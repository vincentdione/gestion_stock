package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.FournisseurController;
import com.ovd.gestionstock.dto.FournisseurDto;
import com.ovd.gestionstock.services.FournisseurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FournisseurApi implements FournisseurController {

    private final FournisseurService fournisseurService;

    @Override
    public ResponseEntity<FournisseurDto> save(FournisseurDto request) {
        return ResponseEntity.ok(fournisseurService.createFournisseur(request));
    }

    @Override
    public ResponseEntity<List<FournisseurDto>> getAllFournisseurs() {
        return ResponseEntity.ok(fournisseurService.getAllFournisseur());
    }

    @Override
    public ResponseEntity<FournisseurDto> getFournisseurById(Long id) {
        return ResponseEntity.ok(fournisseurService.getFournisseurById(id));
    }

    @Override
    public ResponseEntity deleteFournisseur(Long id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.ok().build();
    }
}
