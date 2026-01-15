package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import lombok.Data;

@Data
public class ClientStatusDto {

    private ClientStatus status;

}
