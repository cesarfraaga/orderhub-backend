package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "client id cannot be null")
    @Positive(message = "item id must be greater than zero.")
    private Long clientId;

    private BigDecimal total;

    private LocalDateTime createdAt;

    private List<OrderItemDto> items;

    private OrderStatus status;
}
