package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ProductStatus;
import lombok.Data;

@Data
public class ProductStatusDto {

    private ProductStatus status;

}
