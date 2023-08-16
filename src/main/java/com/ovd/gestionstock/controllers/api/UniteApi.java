package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.UniteController;
import com.ovd.gestionstock.dto.UniteDto;
import com.ovd.gestionstock.services.UniteService;
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
@Tag(name = "unités")
public class UniteApi  implements UniteController {

    private final UniteService uniteService;

    @Override
    public ResponseEntity<UniteDto> saveUnite(UniteDto request) {
        return ResponseEntity.ok(uniteService.createUnite(request));
    }

    @Override
    public ResponseEntity<List<UniteDto>> getAllUnites() {
        return ResponseEntity.ok(uniteService.getAllUnite());
    }

    @Override
    public ResponseEntity<List<UniteDto>> getAllUniteByArticle(Long idArticle) {
        return ResponseEntity.ok(uniteService.findAllByIdArticle(idArticle));
    }

    @Override
    public ResponseEntity<UniteDto> getUniteById(Long idUnite) {
        return ResponseEntity.ok(uniteService.getUniteById(idUnite));
    }

    @Override
    public ResponseEntity<UniteDto> getUniteByNom(String nom) {
        return null;
    }

    @Override
    public ResponseEntity deleteUnite(Long idUnite) {
        uniteService.deleteUnite(idUnite);
        return ResponseEntity.ok().build();
    }
}
