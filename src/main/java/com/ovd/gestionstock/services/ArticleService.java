package com.ovd.gestionstock.services;

import com.ovd.gestionstock.dto.ArticleDto;
import com.ovd.gestionstock.models.Article;

import java.util.List;

public interface ArticleService {

    public List<ArticleDto> getAllArticle();
    public void deleteArticle(Long id);

    public ArticleDto getArticleById(Long id);

    public ArticleDto createArticle(ArticleDto article);
}
