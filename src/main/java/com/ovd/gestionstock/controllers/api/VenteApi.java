package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.VenteController;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.dto.VenteDto;
import com.ovd.gestionstock.services.VenteService;
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
@Tag(name = "ventes")
public class VenteApi implements VenteController {

    private final VenteService venteService;

    @Override
    public ResponseEntity<VenteDto> saveVente(VenteDto request) {
        return ResponseEntity.ok(venteService.createVentes(request));
    }

    @Override
    public ResponseEntity<List<VenteDto>> getAllVentes() {
        return ResponseEntity.ok(venteService.getAllVentes());
    }

    @Override
    public ResponseEntity<VenteDto> getVenteById(Long id) {
        return ResponseEntity.ok(venteService.getVentesById(id));
    }

    @Override
    public ResponseEntity<VenteDto> getVenteByCode(String code) {
        return ResponseEntity.ok(venteService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<LigneVenteDto>> findAllLigneVenteByVenteId(Long idVente) {
             return ResponseEntity.ok(venteService.findAllLigneVentesByVenteId(idVente));
    }



    @Override
    public ResponseEntity deleteVente(Long id) {
        venteService.deleteVentes(id);
        return ResponseEntity.ok().build();
    }
}
