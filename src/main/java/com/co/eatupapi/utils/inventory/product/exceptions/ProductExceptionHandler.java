package com.co.eatupapi.utils.inventory.product.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProductExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ValidationException validationException = new ValidationException(ex.getMessage());
        return buildErrorResponse(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Formato de datos inválido. ";
        if (ex.getMessage().contains("salePrice")) {
            message += "El precio de venta solo acepta números. Ejemplo: 2500.00";
        } else if (ex.getMessage().contains("stock")) {
            message += "El stock solo acepta números. Ejemplo: 45.000";
        } else if (ex.getMessage().contains("startDate")) {
            message += "La fecha de inicio debe tener el formato: YYYY-MM-DD. Ejemplo: 2024-01-10";
        } else if (ex.getMessage().contains("UUID")) {
            message += "El id debe ser un UUID válido.";
        } else {
            message += "Verifica que los campos numéricos solo contengan números y las fechas tengan el formato YYYY-MM-DD";
        }
        return buildRawErrorResponse(message, "INVALID_FORMAT", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildRawErrorResponse(
                "El id '" + ex.getValue() + "' no es un UUID válido.",
                "INVALID_FORMAT",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildRawErrorResponse(
                "El método '" + ex.getMethod() + "' no está soportado para esta ruta.",
                "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        return buildRawErrorResponse(
                "La ruta '" + ex.getResourcePath() + "' no existe",
                "ROUTE_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(ApiException ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getErrorCode());
        body.put("timestamp", ex.getTimestamp());
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildRawErrorResponse(String message, String errorCode, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("errorCode", errorCode);
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }
}