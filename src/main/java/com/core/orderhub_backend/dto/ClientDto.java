package com.core.orderhub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDto {

    private Long id;

    private String name;

    private String cpf;
}
