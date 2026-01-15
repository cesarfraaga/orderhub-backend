package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private Long clientId;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
    private OrderStatus status;

}
