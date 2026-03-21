package com.co.eatupapi.utils.commercial.table.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TableGlobalExceptionHandler {

    @ExceptionHandler(TableValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(TableValidationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TableResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(TableResourceNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TableBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(TableBusinessException ex) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        TableValidationException validationException = new TableValidationException(ex.getMessage());
        return buildErrorResponse(validationException, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(TableApiException ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getErrorCode());
        body.put("timestamp", ex.getTimestamp());
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }
}