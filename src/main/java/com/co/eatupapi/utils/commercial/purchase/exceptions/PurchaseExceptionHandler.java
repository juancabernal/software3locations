package com.co.eatupapi.utils.commercial.purchase.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "com.co.eatupapi.controllers.commercial.purchase")
public class PurchaseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message;

        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        } else {
            message = "Validation failed";
        }

        return build(HttpStatus.BAD_REQUEST,
                PurchaseErrorCode.VALIDATION_ERROR,
                message,
                request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        return build(HttpStatus.BAD_REQUEST,
                PurchaseErrorCode.VALIDATION_ERROR,
                "Request body is missing or malformed",
                request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String msg = String.format("Invalid value '%s' for parameter '%s'",
                ex.getValue(), ex.getName());

        return build(HttpStatus.BAD_REQUEST,
                PurchaseErrorCode.VALIDATION_ERROR,
                msg,
                request);
    }

    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            PurchaseNotFoundException ex, HttpServletRequest request) {

        return build(HttpStatus.NOT_FOUND,
                ex.getErrorCode(),
                ex.getMessage(),
                request);
    }

    @ExceptionHandler(PurchaseConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(
            PurchaseConflictException ex, HttpServletRequest request) {

        return build(HttpStatus.CONFLICT,
                ex.getErrorCode(),
                ex.getMessage(),
                request);
    }

    @ExceptionHandler(PurchaseBusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(
            PurchaseBusinessException ex, HttpServletRequest request) {

        return build(HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getErrorCode(),
                ex.getMessage(),
                request);
    }

    private ResponseEntity<ApiErrorResponse> build(
            HttpStatus status,
            PurchaseErrorCode errorCode,
            String message,
            HttpServletRequest request) {

        ApiErrorResponse body = new ApiErrorResponse(
                errorCode,
                message,
                status.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }
}