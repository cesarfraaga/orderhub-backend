package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDto {

    private Long id;

    @NotBlank(message = "name cannot be null or empty.")
    @Size(min = 2, max = 50, message = "client name cannot be less than 2 or more than 50 characters.")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "client name cannot contain special characters.")
    private String name;

    @NotBlank(message = "cpf cannot be null or empty")
    @Pattern(regexp = "\\d{11}", message = "CPF must have exactly 11 digits.")
    private String cpf;

    //Não preciso da validação aqui já que o status é sempre definido na service
    private ClientStatus status;
}
