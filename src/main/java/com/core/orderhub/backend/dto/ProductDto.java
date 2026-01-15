package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private ProductStatus status;

}
