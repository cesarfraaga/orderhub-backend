package com.core.orderhub.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDto {

    private Long id;

    private String name;

    private String cpf;
}
