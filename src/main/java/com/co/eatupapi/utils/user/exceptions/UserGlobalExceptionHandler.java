package com.co.eatupapi.utils.user.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.co.eatupapi.controllers.user")
public class UserGlobalExceptionHandler {

    private static final String MESSAGE_KEY = "message";
    private static final String ERROR_CODE_KEY = "errorCode";
    private static final String STATUS_KEY = "status";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String FIELD_KEY = "field";
    private static final String ERRORS_KEY = "errors";
    private static final String USER_VALIDATION_ERROR = "USER_VALIDATION_ERROR";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    private static final Pattern REFERENCE_CHAIN_FIELD_PATTERN =
            Pattern.compile("through reference chain: .*\\[\\\"([^\\\"]+)\\\"\\]");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserGlobalExceptionHandler.class);

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(UserValidationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(UserNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(UserBusinessException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(UserAuthenticationException ex) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .toList();

        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, "Request validation failed");
        body.put(ERRORS_KEY, errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<Map<String, Object>> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    Map<String, Object> error = new LinkedHashMap<>();
                    error.put(FIELD_KEY, violation.getPropertyPath().toString());
                    error.put(MESSAGE_KEY, violation.getMessage());
                    return error;
                })
                .toList();

        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, "Request validation failed");
        body.put(ERRORS_KEY, errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameter(MissingServletRequestParameterException ex) {
        String message = "Required parameter '" + ex.getParameterName() + "' is missing";
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, message);
        body.put(FIELD_KEY, ex.getParameterName());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        InvalidFormatException invalidFormat = findCause(ex, InvalidFormatException.class);
        if (invalidFormat != null) {
            String fieldPath = buildFieldPath(invalidFormat);
            String message = buildInvalidFormatMessage(fieldPath, invalidFormat.getTargetType(), invalidFormat.getValue());
            Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, message);
            if (fieldPath != null && !fieldPath.isBlank()) {
                body.put(FIELD_KEY, fieldPath);
            }
            return ResponseEntity.badRequest().body(body);
        }

        MismatchedInputException mismatchedInput = findCause(ex, MismatchedInputException.class);
        if (mismatchedInput != null) {
            String fieldPath = buildFieldPath(mismatchedInput);
            String message = (fieldPath == null || fieldPath.isBlank())
                    ? "Invalid request payload"
                    : "Invalid structure for field '" + fieldPath + "'";
            Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, message);
            if (fieldPath != null && !fieldPath.isBlank()) {
                body.put(FIELD_KEY, fieldPath);
            }
            return ResponseEntity.badRequest().body(body);
        }

        String rawMessage = ex.getMessage() == null ? "" : ex.getMessage();
        String rawLower = rawMessage.toLowerCase();
        String extractedField = extractFieldFromRawMessage(rawMessage);
        if (rawLower.contains("uuid")) {
            String message = buildInvalidFormatMessage(extractedField, UUID.class, "invalid-value");
            Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, message);
            if (extractedField != null) {
                body.put(FIELD_KEY, extractedField);
            }
            return ResponseEntity.badRequest().body(body);
        }
        if (rawLower.contains("localdate") || rawLower.contains("date")) {
            String message = buildInvalidFormatMessage(extractedField, LocalDate.class, "invalid-value");
            Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, message);
            if (extractedField != null) {
                body.put(FIELD_KEY, extractedField);
            }
            return ResponseEntity.badRequest().body(body);
        }

        return ResponseEntity.badRequest()
                .body(baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, "Invalid request payload"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        String message = buildInvalidFormatMessage(fieldName, ex.getRequiredType(), ex.getValue());
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, USER_VALIDATION_ERROR, message);
        body.put(FIELD_KEY, fieldName);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        LOGGER.warn("Data integrity violation in user module", ex);
        return ResponseEntity.badRequest().body(baseBody(
                HttpStatus.BAD_REQUEST,
                "USER_BUSINESS_ERROR",
                "Request could not be processed"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        LOGGER.error("Unhandled exception in user module", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseBody(
                HttpStatus.INTERNAL_SERVER_ERROR,
                INTERNAL_ERROR,
                "An unexpected error occurred, please try again"
        ));
    }

    private Map<String, Object> toFieldError(FieldError fieldError) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put(FIELD_KEY, fieldError.getField());
        error.put(MESSAGE_KEY, fieldError.getDefaultMessage());
        if (fieldError.getRejectedValue() != null) {
            error.put("rejectedValue", fieldError.getRejectedValue());
        }
        return error;
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(UserApiException ex, HttpStatus status) {
        Map<String, Object> body = baseBody(status, ex.getErrorCode(), ex.getMessage());
        body.put(TIMESTAMP_KEY, ex.getTimestamp());
        return ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> baseBody(HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE_KEY, message);
        body.put(ERROR_CODE_KEY, errorCode);
        body.put(STATUS_KEY, status.value());
        body.put(TIMESTAMP_KEY, LocalDateTime.now());
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
                .map(reference -> reference.getFieldName() != null
                        ? reference.getFieldName()
                        : "[" + reference.getIndex() + "]")
                .collect(Collectors.joining("."));
    }

    private String buildInvalidFormatMessage(String fieldName, Class<?> targetType, Object value) {
        String safeField = (fieldName == null || fieldName.isBlank()) ? "unknown" : fieldName;
        String receivedValue = value == null ? "null" : value.toString();

        if (targetType == null) {
            return "Invalid value for field '" + safeField + "'";
        }
        if (UUID.class.equals(targetType)) {
            return "Field '" + safeField + "' must be a valid UUID. Received: '" + receivedValue + "'";
        }
        if (LocalDate.class.equals(targetType)) {
            return "Field '" + safeField + "' must use format YYYY-MM-DD. Received: '" + receivedValue + "'";
        }
        if (targetType.isEnum()) {
            return "Field '" + safeField + "' has an invalid value. Received: '" + receivedValue + "'";
        }
        if (Integer.class.equals(targetType) || int.class.equals(targetType)
                || Long.class.equals(targetType) || long.class.equals(targetType)) {
            return "Field '" + safeField + "' must be a valid integer number. Received: '" + receivedValue + "'";
        }
        if (Boolean.class.equals(targetType) || boolean.class.equals(targetType)) {
            return "Field '" + safeField + "' must be true or false. Received: '" + receivedValue + "'";
        }
        return "Invalid value for field '" + safeField + "'. Received: '" + receivedValue + "'";
    }

    private String extractFieldFromRawMessage(String rawMessage) {
        Matcher matcher = REFERENCE_CHAIN_FIELD_PATTERN.matcher(rawMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
