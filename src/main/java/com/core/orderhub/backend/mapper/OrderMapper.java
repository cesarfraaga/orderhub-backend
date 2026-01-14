package com.core.orderhub.backend.mapper;

import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.domain.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);

    Order toEntity(OrderDto orderDto);

}
