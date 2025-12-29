package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.ArticleController;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.utils.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@Tag(name = "articles")
@RequestMapping("/api/v1/admin")
public class ArticleApi {

    private final ArticleService articleService;

    @PostMapping(value = "/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> saveArticle(@RequestBody ArticleDto request) {
        //ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(request));
        return ResponseEntity.ok(articleService.createArticle(request));
    }

    @GetMapping(value = "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticle());
    }


    @GetMapping(value = "/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable("idArticle") Long idArticle) {
        return ResponseEntity.ok(articleService.getArticleById(idArticle));
    }

    @GetMapping(value = "/articles/historique/ventes/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LigneVenteDto>> findHistoriqueVente(@PathVariable("idArticle") Long idArticle) {
        return ResponseEntity.ok(articleService.findHistoriqueVente(idArticle));
    }

    @GetMapping(value = "/articles/historique/commandeClients/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LigneCommandeClientDto>> findHistoriqueCommandeClient(@PathVariable("idArticle") Long idArticle) {
        return ResponseEntity.ok(articleService.findHistoriqueCommandeClient(idArticle));
    }

    @GetMapping(value = "/articles/historique/commandeFournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Long idArticle) {
        return ResponseEntity.ok(articleService.findHistoriqueCommandeFournisseur(idArticle));
    }

    @GetMapping(value = "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDto>> findHistoriqueByIdCatergory(@PathVariable("idCategory") Long idCategory) {
        return ResponseEntity.ok(articleService.findHistoriqueByIdSousCatergory(idCategory));
    }


    @DeleteMapping(value = Constants.APP_ROOT+"/articles/delete/{idArticle}")
    public ResponseEntity deleteArticle(@PathVariable("idArticle") Long idArticle) {
        articleService.deleteArticle(idArticle);
        return ResponseEntity.ok().build();
    }
}
