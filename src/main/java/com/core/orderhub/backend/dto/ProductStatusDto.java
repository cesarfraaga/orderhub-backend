package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductStatusDto {

    @NotNull(message = "product status cannot be null.")
    private ProductStatus status;

}
