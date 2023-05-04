package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.UtilisateurController;
import com.ovd.gestionstock.dto.UtilisateurDto;
import com.ovd.gestionstock.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UtilisateurApi implements UtilisateurController {

    private final UtilisateurService utilisateurService;
    @Override
    public ResponseEntity<UtilisateurDto> save(UtilisateurDto request) {
        return ResponseEntity.ok(utilisateurService.createUtilisateur(request));
    }

    @Override
    public ResponseEntity<List<UtilisateurDto>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateur());
    }

    @Override
    public ResponseEntity<UtilisateurDto> getUtilisateurById(Long id) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }

    @Override
    public ResponseEntity deleteUtilisateur(Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.ok().build();
    }
}
