package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Long> {

    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumTelOrEmail(
            String nom, String prenom, String numTel, String email);

    List<Client> findByNomAndPrenomAndEmailAndNumTel(String nom, String prenom, String numTel, String email);

    List<Client> findByNomAndPrenom(String nom, String prenom);
    List<Client> findByEmail(String email);
    List<Client> findByNumTel(String numTel);
    List<Client> findByNom(String nom);
    List<Client> findByNomAndEmail(String nom,String email);
    List<Client> findByNomAndNumTel(String nom,String numTel);
    List<Client> findByPrenom(String prenom);



}
