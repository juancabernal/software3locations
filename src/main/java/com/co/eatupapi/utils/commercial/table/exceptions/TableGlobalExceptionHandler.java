package com.co.eatupapi.utils.commercial.table.exceptions;

import com.co.eatupapi.controllers.commercial.table.TableController;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = TableController.class)
public class TableGlobalExceptionHandler {

    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Bogota");

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "La solicitud contiene errores de validación");
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        body.put("fieldErrors", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "La solicitud contiene errores de validación");
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> fieldErrors.put(
                violation.getPropertyPath().toString(),
                violation.getMessage()
        ));
        body.put("fieldErrors", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        InvalidFormatException invalidFormatException = findCause(ex, InvalidFormatException.class);
        if (invalidFormatException != null) {
            String fieldPath = buildFieldPath(invalidFormatException);
            String message = buildInvalidFormatMessage(
                    fieldPath,
                    invalidFormatException.getTargetType(),
                    invalidFormatException.getValue()
            );
            return ResponseEntity.badRequest().body(
                    baseBody(HttpStatus.BAD_REQUEST, "INVALID_FORMAT", message, fieldPath)
            );
        }

        MismatchedInputException mismatchedInputException = findCause(ex, MismatchedInputException.class);
        if (mismatchedInputException != null) {
            String fieldPath = buildFieldPath(mismatchedInputException);
            if (fieldPath != null && !fieldPath.isBlank()) {
                String message = "El campo '" + fieldPath + "' tiene una estructura inválida o un tipo de dato incorrecto";
                return ResponseEntity.badRequest().body(
                        baseBody(HttpStatus.BAD_REQUEST, "INVALID_FORMAT", message, fieldPath)
                );
            }
            return ResponseEntity.badRequest().body(
                    baseBody(HttpStatus.BAD_REQUEST, "INVALID_BODY", "El body de la solicitud es inválido o está vacío")
            );
        }

        return ResponseEntity.badRequest().body(baseBody(
                HttpStatus.BAD_REQUEST,
                "INVALID_BODY",
                "El body de la solicitud es inválido. Verifica tipos de datos, formato de fechas y estructura JSON"
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        Class<?> targetType = ex.getRequiredType();
        String message = buildInvalidFormatMessage(fieldName, targetType, ex.getValue());
        return ResponseEntity.badRequest().body(baseBody(HttpStatus.BAD_REQUEST, "INVALID_FORMAT", message, fieldName));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(baseBody(
                HttpStatus.BAD_REQUEST,
                "MISSING_PARAMETER",
                "El parámetro '" + ex.getParameterName() + "' es requerido",
                ex.getParameterName()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rawMessage = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String normalized = rawMessage != null ? rawMessage.toLowerCase() : "";

        if (normalized.contains("restaurant_tables") && (normalized.contains("venue") || normalized.contains("table_number") || normalized.contains("tableNumber"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(baseBody(
                    HttpStatus.CONFLICT,
                    "UNIQUE_CONSTRAINT_VIOLATION",
                    "Ya existe una mesa activa con el mismo número en la sede indicada"
            ));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(baseBody(
                HttpStatus.CONFLICT,
                "DATA_INTEGRITY_ERROR",
                "No se pudo completar la operación por una restricción de integridad de datos"
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        TableValidationException validationException = new TableValidationException(ex.getMessage());
        return buildErrorResponse(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(baseBody(
                HttpStatus.METHOD_NOT_ALLOWED,
                "METHOD_NOT_ALLOWED",
                "El método '" + ex.getMethod() + "' no está soportado para esta ruta. Métodos permitidos: " + ex.getSupportedHttpMethods()
        ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseBody(
                HttpStatus.NOT_FOUND,
                "ROUTE_NOT_FOUND",
                "La ruta '" + ex.getResourcePath() + "' no existe"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseBody(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Ocurrió un error inesperado en el servidor"
        ));
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(TableApiException ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getErrorCode());
        body.put("timestamp", ex.getTimestamp());
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> baseBody(HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("errorCode", errorCode);
        body.put("timestamp", LocalDateTime.now(BUSINESS_ZONE));
        body.put("status", status.value());
        return body;
    }

    private Map<String, Object> baseBody(HttpStatus status, String errorCode, String message, String field) {
        Map<String, Object> body = baseBody(status, errorCode, message);
        body.put("field", field);
        return body;
    }

    private <T extends Throwable> T findCause(Throwable throwable, Class<T> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }

    private String buildFieldPath(JsonMappingException exception) {
        return exception.getPath().stream()
                .map(reference -> reference.getFieldName() != null ? reference.getFieldName() : "[" + reference.getIndex() + "]")
                .collect(Collectors.joining("."));
    }

    private String buildInvalidFormatMessage(String fieldName, Class<?> targetType, Object value) {
        String safeField = (fieldName == null || fieldName.isBlank()) ? "desconocido" : fieldName;

        if (targetType == null) {
            return "El campo '" + safeField + "' tiene un formato inválido";
        }
        if (Integer.class.equals(targetType) || int.class.equals(targetType)
                || Long.class.equals(targetType) || long.class.equals(targetType)) {
            return "El campo '" + safeField + "' solo acepta números enteros. Valor recibido: '" + value + "'";
        }
        if (Boolean.class.equals(targetType) || boolean.class.equals(targetType)) {
            return "El campo '" + safeField + "' solo acepta true o false. Valor recibido: '" + value + "'";
        }
        if (UUID.class.equals(targetType)) {
            return "El campo '" + safeField + "' debe ser un UUID válido. Valor recibido: '" + value + "'";
        }
        if (java.time.LocalDate.class.equals(targetType)) {
            return "El campo '" + safeField + "' debe tener el formato YYYY-MM-DD. Valor recibido: '" + value + "'";
        }
        if (java.time.LocalTime.class.equals(targetType)) {
            return "El campo '" + safeField + "' debe tener el formato HH:mm:ss. Valor recibido: '" + value + "'";
        }
        if (String.class.equals(targetType)) {
            return "El campo '" + safeField + "' solo acepta texto. Valor recibido: '" + value + "'";
        }
        return "El campo '" + safeField + "' tiene un formato inválido para el tipo esperado '" + targetType.getSimpleName() + "'. Valor recibido: '" + value + "'";
    }
}