package com.core.orderhub.backend.mapper;

import com.core.orderhub.backend.domain.entity.OrderItem;
import com.core.orderhub.backend.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.id", target = "productId")
    OrderItemDto toDto(OrderItem orderItem);
}