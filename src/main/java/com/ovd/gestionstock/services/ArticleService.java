package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.dto.LigneCommandeClientDto;
import com.ovd.gestionstock.dto.LigneCommandeFournisseurDto;
import com.ovd.gestionstock.dto.LigneVenteDto;
import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.LigneCommandeFournisseur;

import java.util.List;

public interface ArticleService {

     List<ArticleDto> getAllArticle();
     List<LigneVenteDto> findHistoriqueVente(Long idArticle);
     List<LigneCommandeClientDto> findHistoriqueCommandeClient(Long idArticle);
     List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Long idArticle);
     List<ArticleDto> findHistoriqueByIdSousCatergory(Long idCategory);
     void deleteArticle(Long id);

     ArticleDto getArticleById(Long id);

    public ArticleDto createArticle(ArticleDto article);
}
