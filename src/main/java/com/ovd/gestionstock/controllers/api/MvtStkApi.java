package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.MvtStkController;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.services.MvtStkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "mouvements de stock")
public class MvtStkApi implements MvtStkController {

    private final MvtStkService service;

    @Override
    public ResponseEntity<BigDecimal> stockReelArticle(Long idArticle) {
        return ResponseEntity.ok(service.stockReelArticle(idArticle));
    }

    @Override
    public ResponseEntity<List<MvtStkDto>> findAllMvtStock() {
        return ResponseEntity.ok(service.getAllMvtStk());
    }

    @Override
    public ResponseEntity<List<MvtStkDto>> mvtStkArticle(Long idArticle) {
        return ResponseEntity.ok(service.mvtStockArticle(idArticle));
    }

    @Override
    public ResponseEntity<MvtStkDto> entreeStock(MvtStkDto dto) {
        return ResponseEntity.ok(service.entreeMvtStk(dto));
    }

    @Override
    public ResponseEntity<MvtStkDto> sortieStock(MvtStkDto dto) {
        return ResponseEntity.ok(service.sortieMvtStk(dto));
    }

    @Override
    public ResponseEntity<MvtStkDto> correctionStockPos(MvtStkDto dto) {
        return ResponseEntity.ok(service.correctionMvtStkPos(dto));
    }

    @Override
    public ResponseEntity<MvtStkDto> correctionStockNeg(MvtStkDto dto) {
        return ResponseEntity.ok(service.correctionMvtStkNeg(dto));
    }
}
