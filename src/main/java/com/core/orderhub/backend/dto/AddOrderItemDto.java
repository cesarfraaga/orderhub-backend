package com.core.orderhub.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddOrderItemDto {

    @NotNull(message = "item id cannot be null.")
    @Positive(message = "item id must be greater than zero.")
    private Long productId;

    @NotNull(message = "item quantity cannot be null.")
    @Positive(message = "item quantity must be greater than zero.")
    private Integer quantity;
}