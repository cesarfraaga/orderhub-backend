package com.core.orderhub.backend.mapper;

import com.core.orderhub.backend.dto.ProductDto;
import com.core.orderhub.backend.domain.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

}
