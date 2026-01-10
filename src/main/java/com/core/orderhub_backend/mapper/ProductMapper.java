package com.core.orderhub_backend.mapper;

import com.core.orderhub_backend.dto.ProductDto;
import com.core.orderhub_backend.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

}
