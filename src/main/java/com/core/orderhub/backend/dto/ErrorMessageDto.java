package com.core.orderhub.backend.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class ErrorMessageDto {

    private final int status;
    private final String message;
    private final String path;

    private LocalDateTime timestamp =   LocalDateTime.now();
}