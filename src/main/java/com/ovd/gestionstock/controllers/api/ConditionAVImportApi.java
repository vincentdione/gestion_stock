package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.dto.ConditionAVImportDto;
import com.ovd.gestionstock.services.ConditionImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/conditions")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "conditions-import")
public class ConditionAVImportApi {

    private static final Logger log = LoggerFactory.getLogger(ConditionAVImportApi.class);
    private final ConditionImportService conditionImportService;

    @PostMapping(value = "/import/csv", consumes = "multipart/form-data")
    public ResponseEntity<List<ConditionAVImportDto>> importConditionCsv(
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(conditionImportService.importConditionsFromCsv(file));
    }
}