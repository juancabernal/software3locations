package com.co.eatupapi.utils.user.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "com.co.eatupapi.controllers.user")
public class UserGlobalExceptionHandler {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Invalid request payload";
        return buildGenericErrorResponse(message, "USER_VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildGenericErrorResponse("Invalid request payload", "USER_VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildGenericErrorResponse("Invalid request parameter value", "USER_VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        LOGGER.error("Unhandled exception in user module", ex);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "An unexpected error occurred, please try again");
        body.put("errorCode", "INTERNAL_ERROR");
        body.put("status", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(UserApiException ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getErrorCode());
        body.put("timestamp", ex.getTimestamp());
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildGenericErrorResponse(String message, String errorCode,
                                                                          HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("errorCode", errorCode);
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }
}
