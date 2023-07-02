package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.EntrepriseController;
import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.services.EntrepriseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "entreprises")
@RequestMapping("/api/v1/admin")
public class EntrepriseApi implements EntrepriseController{

    private final EntrepriseService entrepriseService;

    @Override
    public ResponseEntity<EntrepriseDto> saveEntreprise(EntrepriseDto request) {
        return ResponseEntity.ok(entrepriseService.createEntreprise(request));
    }

    @Override
    public ResponseEntity<List<EntrepriseDto>> getAllEntreprises() {
        return ResponseEntity.ok(entrepriseService.getAllEntreprise());
    }

    @Override
    public ResponseEntity<EntrepriseDto> getEntrepriseById(Long id) {
        return ResponseEntity.ok(entrepriseService.getEntrepriseById(id));
    }

    @Override
    public ResponseEntity deleteEntreprise(Long id) {
        entrepriseService.deleteEntreprise(id);
        return ResponseEntity.ok().build();
    }
}
