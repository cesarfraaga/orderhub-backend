package com.core.orderhub_backend.repository;

import com.core.orderhub_backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
