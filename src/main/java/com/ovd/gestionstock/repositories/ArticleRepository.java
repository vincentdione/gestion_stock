package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends TenantAwareRepository<Article,Long> {

    List<Article> findAllBySousCategoryId(Long idSousCategory);
}
