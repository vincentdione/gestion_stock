package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.ModePayementController;
import com.ovd.gestionstock.dto.ModePayementDto;
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
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "modes Payement")
public class ModePayementApi  implements ModePayementController {

    private final ModePayementService modePayementService;
    @Override
    public ResponseEntity<ModePayementDto> saveMode(ModePayementDto request) {
        return ResponseEntity.ok(modePayementService.createModePayement(request));
    }

    @Override
    public ResponseEntity<List<ModePayementDto>> getAllModes() {
        return ResponseEntity.ok(modePayementService.getAllModePayement());
    }

    @Override
    public ResponseEntity<ModePayementDto> getModeById(Long idMode) {
        return ResponseEntity.ok(modePayementService.getModePayementById(idMode));
    }

    @Override
    public ResponseEntity<ModePayementDto> getModeByNom(String nom) {
        return null;
    }

    @Override
    public ResponseEntity deleteMode(Long idMode) {
        modePayementService.deleteModePayement(idMode);
        return ResponseEntity.ok().build();
    }
}
