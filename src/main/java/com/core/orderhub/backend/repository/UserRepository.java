package com.core.orderhub.backend.repository;

import com.core.orderhub.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
