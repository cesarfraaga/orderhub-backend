package com.core.orderhub.backend.repository;

import com.core.orderhub.backend.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
