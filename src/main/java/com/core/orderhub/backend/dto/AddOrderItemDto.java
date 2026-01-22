package com.core.orderhub.backend.dto;

import lombok.Data;

@Data
public class AddOrderItemDto {

    private Long productId;
    private Integer quantity;

}