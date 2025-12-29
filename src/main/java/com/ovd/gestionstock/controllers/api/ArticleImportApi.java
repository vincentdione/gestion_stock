package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.services.ArticleImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/articles")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "articles-import")
public class ArticleImportApi {

    private static final Logger log = LoggerFactory.getLogger(ArticleImportApi.class);
    private final ArticleImportService articleImportService;

    @PostMapping(value = "/import/csv", consumes = "multipart/form-data")
    public ResponseEntity<List<ArticleDto>> importCsv(
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(articleImportService.importArticlesFromCsv(file));
    }
}