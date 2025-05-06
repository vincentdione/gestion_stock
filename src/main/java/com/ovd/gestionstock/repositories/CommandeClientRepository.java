package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommandeClientRepository extends TenantAwareRepository<CommandeClient,Long> {

    Optional<CommandeClient> findByCode(String code);

    List<CommandeClient> findByClientNomAndClientEmailAndCode(String nom, String email, String codeCommande);

    List<CommandeClient> findByClientNomAndClientEmail(String nom, String email);

    Optional<CommandeClient> findByClientNom(String nom);

    Optional<CommandeClient> findByClientEmail(String email);
}
