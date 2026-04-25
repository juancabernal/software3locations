package com.co.eatupapi.utils.commercial.sales.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(1)
@RestControllerAdvice(basePackages = "com.co.eatupapi.controllers.commercial.sales")
public class SaleExceptionHandler {

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<SaleApiErrorResponse> handleNotFound(SaleNotFoundException ex) {
        SaleApiErrorResponse error = new SaleApiErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SaleBusinessException.class)
    public ResponseEntity<SaleApiErrorResponse> handleBusiness(SaleBusinessException ex) {
        SaleApiErrorResponse error = new SaleApiErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SaleValidationException.class)
    public ResponseEntity<SaleApiErrorResponse> handleValidation(SaleValidationException ex) {
        SaleApiErrorResponse error = new SaleApiErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SaleApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message;
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            message = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        } else if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            message = ex.getBindingResult().getGlobalErrors().getFirst().getDefaultMessage();
        } else {
            message = "Error de validación";
        }
        SaleApiErrorResponse error = new SaleApiErrorResponse(SaleErrorCode.VALIDATION_ERROR, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
