package com.core.orderhub.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrderItemDto {

        @NotNull(message = "product id cannot be null.")
        @Positive(message = "product id must be greater than zero.")
        private Long productId; //preciso validar

        @NotNull(message = "item quantity cannot be null.")
        @Positive(message = "item quantity must be greater than zero.")
        private Integer quantity; //preciso validar

        private String productName;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;

}
