package com.core.orderhub.backend.exception;

import com.core.orderhub.backend.dto.ErrorMessageDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler { //intercepta exceções, decide http status, corpo da resposta e centraliza msgs

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
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
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorMessageDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorMessageDto);
    }

}
