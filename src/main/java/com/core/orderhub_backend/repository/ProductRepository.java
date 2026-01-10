package com.core.orderhub_backend.repository;

import com.core.orderhub_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
