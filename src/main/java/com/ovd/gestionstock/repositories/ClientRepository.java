package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
