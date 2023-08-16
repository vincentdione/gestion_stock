package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.ConditionAVController;
import com.ovd.gestionstock.controllers.UniteController;
import com.ovd.gestionstock.dto.ConditionAVDto;
import com.ovd.gestionstock.dto.UniteDto;
import com.ovd.gestionstock.services.ConditionAVService;
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
@Tag(name = "conditions de ventes")
public class ConditionAVApi implements ConditionAVController {

    private final ConditionAVService conditionAVService;

    @Override
    public ResponseEntity<ConditionAVDto> saveCondition(ConditionAVDto request) {
        return ResponseEntity.ok(conditionAVService.createConditionAV(request));
    }

    @Override
    public ResponseEntity<List<ConditionAVDto>> getAllConditions() {
        return ResponseEntity.ok(conditionAVService.getAllConditionAV());
    }

    @Override
    public ResponseEntity<List<ConditionAVDto>> getAllConditionWithDistincts() {
        return ResponseEntity.ok(conditionAVService.getAllConditionAVWithDistinct());
    }

    @Override
    public ResponseEntity<ConditionAVDto> getConditionById(Long idCondition) {
        return ResponseEntity.ok(conditionAVService.getConditionAVById(idCondition));
    }

    @Override
    public ResponseEntity<ConditionAVDto> getConditionByNom(String nom) {
        return null;
    }

    @Override
    public ResponseEntity deleteCondition(Long idCondition) {
        conditionAVService.deleteConditionAV(idCondition);
        return ResponseEntity.ok().build();
    }
}
