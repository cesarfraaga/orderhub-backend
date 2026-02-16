package com.core.orderhub.backend.mapper;

import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "orderItemList", target = "items")
    OrderDto toDto(Order order);

    Order toEntity(OrderDto orderDto);

}
