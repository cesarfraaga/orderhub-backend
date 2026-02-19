package com.core.orderhub.backend.exception;

import com.core.orderhub.backend.dto.ErrorMessageDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler { //intercepta exceções, decide http status, corpo da resposta e centraliza msgs

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.warn("Resource not found: {}", exception.getMessage());
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageDto);
    }

    @ExceptionHandler(BusinessException.class) //Quando essa exceção (Business) acontecer, vai chamar esse método
    public ResponseEntity<ErrorMessageDto> handleBusiness(
            BusinessException exception,
            HttpServletRequest request
    ) {
        logger.warn("Business error: {}", exception.getMessage());
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorMessageDto);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorMessageDto> handleConflict(
            ResourceConflictException exception,
            HttpServletRequest request
    ) {
        logger.warn("Resource conflict error: {}", exception.getMessage());
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorMessageDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String field = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getField();

        String message = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        logger.warn("Validation error on field '{}' : {}", field, message);

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorMessageDto);
    }
}