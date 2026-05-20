package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends TenantAwareRepository<Article,Long> {

    List<Article> findAllBySousCategoryId(Long idSousCategory);
    List<Article> findAllByCodeArticleInAndIdEntreprise(List<String> codes, Long idEntreprise);
    Optional<Article> findByCodeArticleAndIdEntreprise(String codeArticle, Long idEntreprise);
}
