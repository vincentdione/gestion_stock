package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Article;
import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.models.ConditionAV;
import com.ovd.gestionstock.models.LigneCommandeClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class LigneCommandeClientRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @Mock
    private LigneCommandeClientRepository ligneCommandeClientRepository;

    @Mock
    private CommandeClientRepository commandeClientRepository;

    @Mock
    private ArticleRepository articleRepository;

    private LigneCommandeClient ligne1, ligne2;
    private CommandeClient commande;

//    @DynamicPropertySource
//    static void registerPgProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//    }

//    @Test
//    void testContainerIsRunning() {
//        assertTrue(postgreSQLContainer.isRunning());
//        System.out.println("JDBC URL: " + postgreSQLContainer.getJdbcUrl());
//    }





    @BeforeEach
    void setUp() {
        // Set up entities and save them
        Article article1 = new Article();
        article1.setId(1L);
        article1.setCodeArticle("ART001");

        Article article2 = new Article();
        article2.setId(2L);
        article2.setCodeArticle("ART002");

        articleRepository.save(article1);
        articleRepository.save(article2);


        commande = new CommandeClient();
        commande.setId(1L);
        commande.setCode("CMD001");

        commandeClientRepository.save(commande);

        ligne1 = new LigneCommandeClient();
        ligne1.setId(1L);
        ligne1.setArticle(article1);
        ligne1.setCommandeClient(commande);

        ligne2 = new LigneCommandeClient();
        ligne2.setId(2L);
        ligne2.setArticle(article2);
        ligne2.setCommandeClient(commande);

        ligneCommandeClientRepository.save(ligne1);
        ligneCommandeClientRepository.save(ligne2);
    }

    @AfterEach
    void tearDown() {
        ligneCommandeClientRepository.deleteAll();
        commandeClientRepository.deleteAll();
    }

//    @Test
//    void findAllByCommandeClientId() {
//        // Assurez-vous que les données sont bien persistées
//        assertNotNull(ligne1.getId(), "LigneCommandeClient ligne1 should have a valid ID");
//        assertNotNull(ligne2.getId(), "LigneCommandeClient ligne2 should have a valid ID");
//
//        // Exécutez la requête
//        List<LigneCommandeClient> result = ligneCommandeClientRepository.findAllByCommandeClientId(commande.getId());
//
//        // Vérifiez que le résultat n'est pas null
//        assertNotNull(result, "Result should not be null");
//
//        // Vérifiez la taille du résultat
//        assertEquals(2, result.size(), "Expected 2 LigneCommandeClient entities to be found");
//
//        // Vérifiez que les entités sont présentes
//        assertTrue(result.contains(ligne1), "Expected result to contain ligne1");
//        assertTrue(result.contains(ligne2), "Expected result to contain ligne2");
//    }

//    @Test
//    void findAllByArticleId() {
//        List<LigneCommandeClient> result = ligneCommandeClientRepository.findAllByArticleId(1L);
//
//        assertNotNull(result);
//        assertEquals(1, result.size(), "Expected 1 LigneCommandeClient entity to be found");
//        assertEquals(ligne1, result.get(0), "Expected result to contain ligne1");
//    }
}