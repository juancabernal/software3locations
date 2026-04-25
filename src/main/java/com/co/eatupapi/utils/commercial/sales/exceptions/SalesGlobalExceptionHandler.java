package com.co.eatupapi.utils.commercial.sales.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.co.eatupapi.controllers.commercial.sales")
public class SalesGlobalExceptionHandler {

    @ExceptionHandler(SaleValidationException.class)
    public ResponseEntity<SaleApiErrorResponse> handleValidationException(SaleValidationException ex) {
        return buildErrorResponse(ex.getMessage(), ex.getErrorCode().name(), ex.getTimestamp(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<SaleApiErrorResponse> handleNotFoundException(SaleNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), ex.getErrorCode().name(), ex.getTimestamp(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SaleBusinessException.class)
    public ResponseEntity<SaleApiErrorResponse> handleBusinessException(SaleBusinessException ex) {
        return buildErrorResponse(ex.getMessage(), ex.getErrorCode().name(), ex.getTimestamp(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SaleApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message;
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            message = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        } else if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            message = ex.getBindingResult().getGlobalErrors().getFirst().getDefaultMessage();
        } else {
            message = "Error de validación";
        }

        return buildErrorResponse(message, SaleErrorCode.VALIDATION_ERROR.name(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SaleApiErrorResponse> handleUnexpectedException(Exception ex) {
        return buildErrorResponse(
                "Error interno del servidor",
                SaleErrorCode.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<SaleApiErrorResponse> buildErrorResponse(
            String message,
            String errorCode,
            LocalDateTime timestamp,
            HttpStatus status
    ) {
        SaleApiErrorResponse body = new SaleApiErrorResponse(message, errorCode, timestamp, status.value());
        return ResponseEntity.status(status).body(body);
    }
}
