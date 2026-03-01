package com.core.orderhub.backend.domain.entity;

import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public void decreaseStock(Integer quantity) {
        if (this.status != ProductStatus.ACTIVE) {
            throw new BusinessException("Product is not active");
        }

        if (quantity > this.quantity) {
            throw new BusinessException("Insufficient stock");
        }
        this.quantity -= quantity;
    }

    public void increaseStock(Integer quantity) {
        this.quantity += quantity;
    }

    public boolean isActive() {
        return this.status == ProductStatus.ACTIVE;
    }
}