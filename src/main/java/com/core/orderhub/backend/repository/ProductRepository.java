package com.core.orderhub.backend.repository;

import com.core.orderhub.backend.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
