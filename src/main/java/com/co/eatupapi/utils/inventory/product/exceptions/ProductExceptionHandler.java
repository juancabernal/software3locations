package com.co.eatupapi.utils.inventory.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
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

    // Texto en campo numérico o fecha con formato incorrecto
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
            message += "El id debe ser un UUID válido. Ejemplo: a3f8c1d2-44b7-4e9a-bc12-ff3301882abc";
        } else {
            message += "Verifica que los campos numéricos solo contengan números y las fechas tengan el formato YYYY-MM-DD";
        }

        return buildRawErrorResponse(message, "INVALID_FORMAT", HttpStatus.BAD_REQUEST);
    }

    // ID en la URL no es un UUID válido
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildRawErrorResponse(
                "El id '" + ex.getValue() + "' no es un UUID válido. Ejemplo: a3f8c1d2-44b7-4e9a-bc12-ff3301882abc",
                "INVALID_FORMAT",
                HttpStatus.BAD_REQUEST
        );
    }

    // Método HTTP no soportado — ej: PATCH en lugar de PUT
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildRawErrorResponse(
                "El método '" + ex.getMethod() + "' no está soportado para esta ruta. Métodos permitidos: " + ex.getSupportedHttpMethods(),
                "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    // Ruta no existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        return buildRawErrorResponse(
                "La ruta '" + ex.getResourcePath() + "' no existe",
                "ROUTE_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }

    // Cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildRawErrorResponse(
                "Ocurrió un error inesperado en el servidor",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // ── Builders ─────────────────────────────────────────

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