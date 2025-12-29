package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.LivraisonController;
import com.ovd.gestionstock.dto.LivraisonDto;
import com.ovd.gestionstock.services.LivraisonService;
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
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','LIVREUR')")
@Tag(name = "Livraisons")
public class LivraisonApi implements LivraisonController {

    private final LivraisonService service;

    @Override
    public ResponseEntity<List<LivraisonDto>> getAllLivraisons() {
        return ResponseEntity.ok(service.getAllLivraisons());
    }

    @Override
    public ResponseEntity<LivraisonDto> getLivraisonById(Long idLivraison) {
        return ResponseEntity.ok(service.getLivraisonById(idLivraison));
    }

    @Override
    public ResponseEntity deleteLivraison(Long idLivraison) {
        service.deleteLivraison(idLivraison);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<LivraisonDto> updateStatut(Long id, String etat) {
        return ResponseEntity.ok(service.updateStatut(id,etat));
    }

    @Override
    public ResponseEntity<LivraisonDto> affecterLivraison(Long livraisonId) {
        return ResponseEntity.ok(service.affecterLivraison(livraisonId));
    }

    @Override
    public ResponseEntity<List<LivraisonDto>> voirLivraisonsUtilisateurEnCours() {
        return ResponseEntity.ok(service.voirLivraisonsUtilisateurEnCours());
    }

    @Override
    public ResponseEntity<List<LivraisonDto>> getLivraisonsByStatut(String statut) {
        return ResponseEntity.ok(service.getLivraisonsByStatut(statut));
    }
}
