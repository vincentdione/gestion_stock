package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.LivraisonController;
import com.ovd.gestionstock.controllers.ModePayementController;
import com.ovd.gestionstock.dto.LivraisonDto;
import com.ovd.gestionstock.dto.ModePayementDto;
import com.ovd.gestionstock.services.LivraisonService;
import com.ovd.gestionstock.services.ModePayementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Livraisons")
public class LivraisonApi implements LivraisonController {

    private final LivraisonService service;

    @Override
    public ResponseEntity<List<LivraisonDto>> getAllLivraisons() {
        return null;
    }

    @Override
    public ResponseEntity<LivraisonDto> getLivraisonById(Long idLivraison) {
        return null;
    }

    @Override
    public ResponseEntity deleteLivraison(Long idLivraison) {
        return null;
    }
}
