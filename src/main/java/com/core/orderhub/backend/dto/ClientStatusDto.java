package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientStatusDto {

    @NotNull(message = "client status cannot be null.")
    private ClientStatus status;

}
