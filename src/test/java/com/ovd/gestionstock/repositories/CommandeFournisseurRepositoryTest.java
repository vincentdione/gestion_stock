package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeFournisseur;
import com.ovd.gestionstock.models.Fournisseur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommandeFournisseurRepositoryTest {

    @Mock
    private CommandeFournisseurRepository commandeFournisseurRepository;

    private CommandeFournisseur commandeFournisseur1, commandeFournisseur2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Fournisseur fournisseur1 = new Fournisseur();
        fournisseur1.setNom("Doe Supplies");
        fournisseur1.setEmail("contact@doe-supplies.com");

        Fournisseur fournisseur2 = new Fournisseur();
        fournisseur2.setNom("Smith Wholesale");
        fournisseur2.setEmail("info@smith-wholesale.com");

        commandeFournisseur1 = new CommandeFournisseur();
        commandeFournisseur1.setCode("CF001");
        commandeFournisseur1.setFournisseur(fournisseur1);

        commandeFournisseur2 = new CommandeFournisseur();
        commandeFournisseur2.setCode("CF002");
        commandeFournisseur2.setFournisseur(fournisseur2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findCommandeFournisseurByCode() {
        when(commandeFournisseurRepository.findCommandeFournisseurByCode("CF001"))
                .thenReturn(Optional.of(commandeFournisseur1));

        Optional<CommandeFournisseur> result = commandeFournisseurRepository.findCommandeFournisseurByCode("CF001");

        assertTrue(result.isPresent());
        assertEquals(commandeFournisseur1, result.get());
    }

    @Test
    void findAllByFournisseurId() {
        when(commandeFournisseurRepository.findAllByFournisseurId(1L))
                .thenReturn(Arrays.asList(commandeFournisseur1));

        List<CommandeFournisseur> result = commandeFournisseurRepository.findAllByFournisseurId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commandeFournisseur1, result.get(0));
    }

    @Test
    void findByFournisseurNomAndFournisseurEmailAndCode() {
        when(commandeFournisseurRepository.findByFournisseurNomAndFournisseurEmailAndCode(
                "Doe Supplies", "contact@doe-supplies.com", "CF001"))
                .thenReturn(Arrays.asList(commandeFournisseur1));

        List<CommandeFournisseur> result = commandeFournisseurRepository.findByFournisseurNomAndFournisseurEmailAndCode(
                "Doe Supplies", "contact@doe-supplies.com", "CF001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commandeFournisseur1, result.get(0));
    }

    @Test
    void findByFournisseurNomAndFournisseurEmail() {
        when(commandeFournisseurRepository.findByFournisseurNomAndFournisseurEmail(
                "Doe Supplies", "contact@doe-supplies.com"))
                .thenReturn(Arrays.asList(commandeFournisseur1));

        List<CommandeFournisseur> result = commandeFournisseurRepository.findByFournisseurNomAndFournisseurEmail(
                "Doe Supplies", "contact@doe-supplies.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commandeFournisseur1, result.get(0));
    }

    @Test
    void findByCode() {
        when(commandeFournisseurRepository.findByCode("CF001"))
                .thenReturn(Arrays.asList(commandeFournisseur1));

        List<CommandeFournisseur> result = commandeFournisseurRepository.findByCode("CF001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commandeFournisseur1, result.get(0));
    }
}