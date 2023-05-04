package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface ArticleController {


    @PostMapping(value = Constants.APP_ROOT+"/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> save(@RequestBody ArticleDto request);

    @GetMapping(value = Constants.APP_ROOT+"/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDto>> getAllArticles();

    @GetMapping(value = Constants.APP_ROOT+"/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable("idArticle") Long id);

//    @GetMapping(value = Constants.APP_ROOT+"/articles/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ArticleDto> getArticleByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = Constants.APP_ROOT+"/articles/delete/{idArticle}")
    public ResponseEntity deleteArticle(@PathVariable("idCategory") Long id);


}
