package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
