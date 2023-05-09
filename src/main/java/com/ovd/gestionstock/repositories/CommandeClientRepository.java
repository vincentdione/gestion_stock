package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommandeClientRepository extends JpaRepository<CommandeClient,Long> {

    Optional<CommandeClient> findByCode(String code);

}
