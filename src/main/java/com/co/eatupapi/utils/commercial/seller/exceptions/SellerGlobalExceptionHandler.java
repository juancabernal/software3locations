package com.co.eatupapi.utils.commercial.seller.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SellerGlobalExceptionHandler {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ERROR_CODE = "errorCode";
    private static final String KEY_STATUS = "status";

    @ExceptionHandler(SellerValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(SellerValidationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(SellerNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SellerBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(SellerBusinessException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_MESSAGE, ex.getMessage());
        body.put(KEY_ERROR_CODE, "SELLER_INVALID_ARGUMENT");
        body.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_MESSAGE, "An unexpected error occurred, please try again");
        body.put(KEY_ERROR_CODE, "INTERNAL_ERROR");
        body.put(KEY_STATUS, 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(SellerApiException ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_MESSAGE, ex.getMessage());
        body.put(KEY_ERROR_CODE, ex.getErrorCode());
        body.put(KEY_STATUS, status.value());
        return ResponseEntity.status(status).body(body);
    }
}
