package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {

    private OrderStatus status;

}
