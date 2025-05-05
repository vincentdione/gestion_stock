package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.SousCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ArticleRepositoryTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private SousCategoryRepository sousCategoryRepository;


    private SousCategory sousCategory;

    @BeforeEach
    void setUp() {
        // Initialize SousCategory
        sousCategory = SousCategory.builder()
                .code("ELEC")
                .designation("Electronics")
                .build();

        // Initialize Article
        Article article = Article.builder()
                .codeArticle("ART1234")
                .designation("Smartphone")
                .prixUnitaireHt(new BigDecimal("500"))
                .tauxTval(new BigDecimal("20"))
                .prixUnitaireTtc(new BigDecimal("600"))
                .photo("photo.jpg")
                .idEntreprise(1L)
                .sousCategory(sousCategory)
                .build();

        // Mock the repository methods
        when(articleRepository.findAllBySousCategoryId(sousCategory.getId())).thenReturn(Collections.singletonList(article));
    }

    @Test
    void shouldFindAllBySousCategoryId() {
        // when
        List<Article> foundArticles = articleRepository.findAllBySousCategoryId(sousCategory.getId());

        // then
        assertNotNull(foundArticles);
        assertEquals(1, foundArticles.size());
        assertEquals("Smartphone", foundArticles.get(0).getDesignation());

        // Verify that the method was called once
        verify(articleRepository, times(1)).findAllBySousCategoryId(sousCategory.getId());
    }
}