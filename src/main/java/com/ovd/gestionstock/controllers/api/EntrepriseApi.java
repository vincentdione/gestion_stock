package com.ovd.gestionstock.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovd.gestionstock.controllers.EntrepriseController;
import com.ovd.gestionstock.dto.EntrepriseDto;
import com.ovd.gestionstock.services.EntrepriseService;
import com.ovd.gestionstock.validators.EntrepriseValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "entreprises")
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','MANAGER')")
@Slf4j
public class EntrepriseApi implements EntrepriseController{

    private final EntrepriseService entrepriseService;
    private final ObjectMapper objectMapper;

//    @Override
//    @PostMapping(value = "/entreprises", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<EntrepriseDto> saveEntreprise(
//            @RequestPart("entreprise") String entrepriseJson,
//            @RequestPart(name = "logoFile", required = false) MultipartFile logoFile
//    ) {
//        log.info("=======================Content-Type: {}========================");
//
//        try {
//            // Désérialisation du JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            EntrepriseDto request = objectMapper.readValue(entrepriseJson, EntrepriseDto.class);
//
//            log.info("Received entreprise: {}", request);
//            log.info("Received logo file: {}", logoFile != null ? logoFile.getOriginalFilename() : "null");
//
//            // Validation de l'entreprise
//            List<String> errors = EntrepriseValidator.validate(request);
//            if (!errors.isEmpty()) {
//                log.error("Invalid entreprise data: {}", errors);
//                return ResponseEntity.badRequest().build();
//            }
//
//            // Traitement du logo
//            if (logoFile != null && !logoFile.isEmpty()) {
//                try {
//                    request.setLogo(logoFile.getBytes());
//                } catch (IOException e) {
//                    log.error("Error reading logo file", e);
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                }
//            }
//
//            // Enregistrement
//            EntrepriseDto savedEntreprise = entrepriseService.createEntreprise(request, logoFile);
//            return ResponseEntity.ok(savedEntreprise);
//
//        } catch (JsonProcessingException e) {
//            log.error("Error parsing entreprise JSON", e);
//            return ResponseEntity.badRequest().build();
//        } catch (Exception e) {
//            log.error("Unexpected error", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }



    @Override
    @PostMapping(value = "/entreprises", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntrepriseDto> saveEntreprise(@RequestBody EntrepriseDto request) {
        log.info("Tentative de création d'entreprise SANS logo");

        log.info(String.valueOf(request));
        // Validation
        List<String> errors = EntrepriseValidator.validate(request);
        if (!errors.isEmpty()) {
            log.error("Données entreprise invalides: {}", errors);
            return ResponseEntity.badRequest().build();
        }

        try {
            // Création de l'entreprise SANS logo
            EntrepriseDto savedEntreprise = entrepriseService.createEntreprise(request);
            return ResponseEntity.ok(savedEntreprise);
        } catch (Exception e) {
            log.error("Erreur inattendue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
