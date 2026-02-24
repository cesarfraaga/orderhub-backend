package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "product name cannot be null or empty.")
    @Size(min = 3, max = 50, message = "product name cannot be less than 3 or more than 50 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s]+$", message = "product name cannot contain special characters.")
    private String name;

    @NotNull(message = "product price cannot be null.")
    @DecimalMin(value = "0.01", message = "product price must be greater than zero.")
    @DecimalMax(value = "100000.00", message = "Price must be less than or equal to 100000.")
    private BigDecimal price;

    @NotBlank(message = "product description cannot be null or empty.")
    @Size(min = 3, max = 100, message = "product description cannot be less than 3 or more than 100 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s]+$", message = "product description cannot contain special characters.")
    private String description;

    @NotNull(message = "quantity cannot be null.")
    @Positive(message = "quantity must be greater than zero.")
    @Max(value = 1000, message = "quantity cannot be greater than 1000.")
    private Integer quantity;

    private ProductStatus status;
}
