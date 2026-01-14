package com.core.orderhub.backend.dto;

import java.math.BigDecimal;

public class OrderItemDto {

        private Long productId;
        private String productName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;

}
