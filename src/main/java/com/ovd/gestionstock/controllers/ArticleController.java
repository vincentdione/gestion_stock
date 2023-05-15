package com.ovd.gestionstock.controllers;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.utils.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ADMIN')")
public interface ArticleController {

    @PostMapping(value = "/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> saveArticle(@RequestBody ArticleDto request);

    @GetMapping(value = "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDto>> getAllArticles();

    @GetMapping(value = "/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = "/articles/historique/ventes/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneVenteDto>> findHistoriqueVente(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = "/articles/historique/commandeClients/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeClientDto>> findHistoriqueCommandeClient(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = "/articles/historique/commandeFournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeFournisseurDto>> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Long idArticle);
    @GetMapping(value = "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ArticleDto>> findHistoriqueByIdCatergory(@PathVariable("idCategory") Long idCategory);

//    @GetMapping(value = "/articles/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ArticleDto> getArticleByNom(@PathVariable("nom") String nom);

    @DeleteMapping(value = Constants.APP_ROOT+"/articles/delete/{idArticle}")
    public ResponseEntity deleteArticle(@PathVariable("idArticle") Long idArticle);


}
