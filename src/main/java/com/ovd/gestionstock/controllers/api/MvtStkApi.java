package com.ovd.gestionstock.controllers.api;

import com.ovd.gestionstock.controllers.MvtStkController;
import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.MvtStkDto;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.ArticleStockInfo;
import com.ovd.gestionstock.models.ArticleStockInfoDto;
import com.ovd.gestionstock.services.ArticleService;
import com.ovd.gestionstock.services.MvtStkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "mouvements de stock")
public class MvtStkApi implements MvtStkController {

    private final MvtStkService service;

    private  final ArticleService articleService;

    @Override
    public ResponseEntity<BigDecimal> stockReelArticle(Long idArticle) {
        return ResponseEntity.ok(service.stockReelArticle(idArticle));
    }

    @Override
    public ResponseEntity<Map<String, BigDecimal>> getStockReelArticleByUnite(Long idArticle) {
        Map<String, BigDecimal> stockByUnite = service.stockReelArticleByUnite(idArticle);
        return ResponseEntity.ok(stockByUnite);
    }

    @Override
    public ResponseEntity<List<MvtStkDto>> findAllMvtStock() {
        return ResponseEntity.ok(service.getAllMvtStk());
    }

    @Override
    public ResponseEntity<Map<String, BigDecimal>> getStockInfo(Long idArticle) {
        Map<String, BigDecimal> stockInfo = new HashMap<>();

        // Rechercher le stock total (somme de la quantité) pour le produit donné
        BigDecimal stockTotal = service.stockReelArticle(idArticle);

        // Rechercher le total du stock vendu (somme de la quantité avec TypeMvtStk "SORTIE") pour le produit donné
        BigDecimal stockVendu = service.stockVenduArticle(idArticle);

        stockInfo.put("stockRestant", stockTotal);
        stockInfo.put("stockVendu", stockVendu);

        return ResponseEntity.ok(stockInfo);
    }

    @Override
    public ResponseEntity<List<ArticleStockInfo>>  getStockInfoByUnite(Long idArticle, String type) {

        List<ArticleDto> articles = articleService.getAllArticle(); // Récupérez tous les articles depuis la base de données
        List<ArticleStockInfo> articleStockInfoList = new ArrayList<>();

        for (ArticleDto article : articles) {
            if(article.getId().equals(idArticle)){
                Map<String, BigDecimal> stockInfo = service.stockVenduArticleByUnite(article.getId(), type);
                ArticleStockInfo articleStockInfo = new ArticleStockInfo(article.getCodeArticle(), stockInfo);
                articleStockInfoList.add(articleStockInfo);
                break;
            }
        }

        return  ResponseEntity.ok(articleStockInfoList);

//        Map<String, BigDecimal> stockInfoByType = new HashMap<>();
//
//        // Rechercher le total du stock vendu par unité pour le produit donné et le type spécifié
//        Map<String, BigDecimal> stockVenduByUnite = service.stockVenduArticleByUnite(idArticle, type);
//
//        stockInfoByType.putAll(stockVenduByUnite);
        //return ResponseEntity.ok(stockInfoByType);
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

    @Override
    public ResponseEntity<List<ArticleStockInfoDto>> getAllArticlesWithStockInfo() {
        List<ArticleDto> articles = articleService.getAllArticle();

        List<ArticleStockInfoDto> articleStockInfoList = new ArrayList<>();

        for (ArticleDto article: articles){
            Map<String, BigDecimal> totalStock =  service.stockReelArticleByUnite(article.getId());
            Map<String, BigDecimal> stockRestant =  service.stockVenduArticleByUnite(article.getId(),"SORTIE");

            ArticleStockInfoDto articleStockInfo = new ArticleStockInfoDto();
            articleStockInfo.setId(article.getId());
            articleStockInfo.setName(article.getCodeArticle());
            articleStockInfo.setTotalStockByUnit(totalStock);
            articleStockInfo.setRemainingStockByUnit(stockRestant);

            articleStockInfoList.add(articleStockInfo);
        }

        return ResponseEntity.ok(articleStockInfoList);
    }
}
