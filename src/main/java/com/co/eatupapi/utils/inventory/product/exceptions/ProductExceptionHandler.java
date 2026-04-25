package com.co.eatupapi.utils.inventory.product.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = "Datos de entrada invalidos";
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        if (fieldError != null && fieldError.getDefaultMessage() != null) {
            message = fieldError.getDefaultMessage();
        }

        return buildRawErrorResponse(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ValidationException validationException = new ValidationException(ex.getMessage());
        return buildErrorResponse(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String rawMessage = ex.getMessage();
        String message = "Formato de datos invalido. ";

        if (rawMessage != null && rawMessage.contains("salePrice")) {
            message += "El precio de venta solo acepta numeros. Ejemplo: 2500.00";
        } else if (rawMessage != null && rawMessage.contains("stock")) {
            message += "El stock solo acepta numeros. Ejemplo: 45.000";
        } else if (rawMessage != null && rawMessage.contains("startDate")) {
            message += "La fecha de inicio debe tener el formato YYYY-MM-DD. Ejemplo: 2024-01-10";
        } else if (rawMessage != null && rawMessage.contains("UUID")) {
            message += "El id debe ser un UUID valido.";
        } else {
            message += "Verifica que los campos numericos solo contengan numeros y las fechas tengan el formato YYYY-MM-DD";
        }

        return buildRawErrorResponse(message, "INVALID_FORMAT", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildRawErrorResponse(
                "El id '" + ex.getValue() + "' no es un UUID valido.",
                "INVALID_FORMAT",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildRawErrorResponse(
                "El metodo '" + ex.getMethod() + "' no esta soportado para esta ruta.",
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
