package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ClientRepositoryTest {

    @Mock
    ClientRepository clientRepository;

    private Client client1, client2;

    @BeforeEach
    void setUp() {
        client1 = new Client();
        client1.setId(1L);
        client1.setNom("Doe");
        client1.setPrenom("John");
        client1.setEmail("john.doe@example.com");
        client1.setNumTel("0123456789");

        client2 = new Client();
        client2.setId(2L);
        client2.setNom("Smith");
        client2.setPrenom("Jane");
        client2.setEmail("jane.smith@example.com");
        client2.setNumTel("0987654321");
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumTelOrEmail() {
        when(clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumTelOrEmail(
                "doe", "john", "0123456789", "john.doe@example.com"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumTelOrEmail(
                "doe", "john", "0123456789", "john.doe@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByNomAndPrenomAndEmailAndNumTel() {
        when(clientRepository.findByNomAndPrenomAndEmailAndNumTel(
                "Doe", "John", "0123456789", "john.doe@example.com"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNomAndPrenomAndEmailAndNumTel(
                "Doe", "John", "0123456789", "john.doe@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByNomAndPrenom() {
        when(clientRepository.findByNomAndPrenom("Doe", "John"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNomAndPrenom("Doe", "John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByEmail() {
        when(clientRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByNumTel() {
        when(clientRepository.findByNumTel("0123456789"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNumTel("0123456789");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByNom() {
        when(clientRepository.findByNom("Doe"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNom("Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByNomAndEmail() {
        when(clientRepository.findByNomAndEmail("Doe", "john.doe@example.com"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNomAndEmail("Doe", "john.doe@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByNomAndNumTel() {
        when(clientRepository.findByNomAndNumTel("Doe", "0123456789"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByNomAndNumTel("Doe", "0123456789");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @Test
    void findByPrenom() {
        when(clientRepository.findByPrenom("John"))
                .thenReturn(Arrays.asList(client1));

        List<Client> result = clientRepository.findByPrenom("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }
}