package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.ConditionAV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConditionAVRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Mock
    private ConditionAVRepository conditionAVRepository;

    private ConditionAV condition1, condition2;


    @Test
    void testContainerIsRunning() {
        assertTrue(postgreSQLContainer.isRunning());
        System.out.println("JDBC URL: " + postgreSQLContainer.getJdbcUrl());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Article article1 = new Article();
        article1.setId(1L);
        article1.setCodeArticle("ART001");

        Article article2 = new Article();
        article2.setId(2L);
        article2.setCodeArticle("ART002");

        condition1 = new ConditionAV();
        condition1.setId(1L);
        condition1.setArticle(article1);

        condition2 = new ConditionAV();
        condition2.setId(2L);
        condition2.setArticle(article2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByArticleId() {
        when(conditionAVRepository.findByArticleId(1L))
                .thenReturn(Arrays.asList(condition1));

        List<ConditionAV> result = conditionAVRepository.findByArticleId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(condition1, result.get(0));
    }
}