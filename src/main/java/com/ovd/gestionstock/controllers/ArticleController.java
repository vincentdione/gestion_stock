package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.utils.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
public interface ArticleController {

    @PostMapping(value = Constants.APP_ROOT+"/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> save(@RequestBody ArticleDto request);

    @GetMapping(value = Constants.APP_ROOT+"/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDto>> getAllArticles();

    @GetMapping(value = Constants.APP_ROOT+"/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = Constants.APP_ROOT+"/articles/historique/ventes/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneVenteDto>> findHistoriqueVente(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = Constants.APP_ROOT+"/articles/historique/commandeClients/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeClientDto>> findHistoriqueCommandeClient(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = Constants.APP_ROOT+"/articles/historique/commandeFournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeFournisseurDto>> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = Constants.APP_ROOT+"/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ArticleDto>> findHistoriqueByIdCatergory(@PathVariable("idCategory") Long idCategory);

//    @GetMapping(value = Constants.APP_ROOT+"/articles/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ArticleDto> getArticleByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = Constants.APP_ROOT+"/articles/delete/{idArticle}")
    public ResponseEntity deleteArticle(@PathVariable("idArticle") Long idArticle);


}
