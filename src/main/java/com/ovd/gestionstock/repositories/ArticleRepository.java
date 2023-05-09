package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    List<Article> findAllByCategoryId(Long idCategory);
}
