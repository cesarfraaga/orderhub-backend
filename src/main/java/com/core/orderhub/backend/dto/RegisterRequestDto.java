package com.core.orderhub.backend.dto;

import com.core.orderhub.backend.domain.enums.UserRoles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    private String email;
    private String password;
    private UserRoles role;
}