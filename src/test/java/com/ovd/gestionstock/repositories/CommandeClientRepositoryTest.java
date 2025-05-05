package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Client;
import com.ovd.gestionstock.models.CommandeClient;
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
class CommandeClientRepositoryTest {


    @Mock
    private CommandeClientRepository commandeClientRepository;

    private CommandeClient commande1, commande2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Client client1 = new Client();
        client1.setNom("Doe");
        client1.setEmail("john.doe@example.com");

        Client client2 = new Client();
        client2.setNom("Smith");
        client2.setEmail("jane.smith@example.com");

        commande1 = new CommandeClient();
        commande1.setCode("CMD001");
        commande1.setClient(client1);

        commande2 = new CommandeClient();
        commande2.setCode("CMD002");
        commande2.setClient(client2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByCode() {
        when(commandeClientRepository.findByCode("CMD001"))
                .thenReturn(Optional.of(commande1));

        Optional<CommandeClient> result = commandeClientRepository.findByCode("CMD001");

        assertTrue(result.isPresent());
        assertEquals(commande1, result.get());
    }

    @Test
    void findByClientNomAndClientEmailAndCode() {
        when(commandeClientRepository.findByClientNomAndClientEmailAndCode(
                "Doe", "john.doe@example.com", "CMD001"))
                .thenReturn(Arrays.asList(commande1));

        List<CommandeClient> result = commandeClientRepository.findByClientNomAndClientEmailAndCode(
                "Doe", "john.doe@example.com", "CMD001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commande1, result.get(0));
    }

    @Test
    void findByClientNomAndClientEmail() {
        when(commandeClientRepository.findByClientNomAndClientEmail(
                "Doe", "john.doe@example.com"))
                .thenReturn(Arrays.asList(commande1));

        List<CommandeClient> result = commandeClientRepository.findByClientNomAndClientEmail(
                "Doe", "john.doe@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commande1, result.get(0));
    }

    @Test
    void findByClientNom() {
        when(commandeClientRepository.findByClientNom("Doe"))
                .thenReturn(Optional.of(commande1));

        Optional<CommandeClient> result = commandeClientRepository.findByClientNom("Doe");

        assertTrue(result.isPresent());
        assertEquals(commande1, result.get());
    }

    @Test
    void findByClientEmail() {
        when(commandeClientRepository.findByClientEmail("john.doe@example.com"))
                .thenReturn(Optional.of(commande1));

        Optional<CommandeClient> result = commandeClientRepository.findByClientEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals(commande1, result.get());
    }

}