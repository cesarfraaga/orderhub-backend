package com.core.orderhub_backend.entity;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class OrderItem {


    private Long id;

    private Order order;

    private Product product;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;

}
