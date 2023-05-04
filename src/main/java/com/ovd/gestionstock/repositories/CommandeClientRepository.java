package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeClientRepository extends JpaRepository<CommandeClient,Long> {
}
