package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.ArticleController;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','USER','MANAGEMENT')")
public class ArticleApi implements ArticleController {

    private final ArticleService articleService;

    @Override
    public ResponseEntity<ArticleDto> save(ArticleDto request) {
        //ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(request));
        return ResponseEntity.ok(articleService.createArticle(request));
    }

    @Override
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticle());
    }

    @Override
    public ResponseEntity<ArticleDto> getArticleById(Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }



    @Override
    public ResponseEntity deleteArticle(Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }
}
