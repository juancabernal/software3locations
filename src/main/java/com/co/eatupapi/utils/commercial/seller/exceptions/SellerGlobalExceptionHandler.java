package com.co.eatupapi.utils.commercial.seller.exceptions;

import com.co.eatupapi.controllers.commercial.seller.SellerController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
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

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = SellerController.class)
public class SellerGlobalExceptionHandler {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ERROR_CODE = "errorCode";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TIMESTAMP = "timestamp";

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
        return buildRawErrorResponse(ex.getMessage(), "SELLER_INVALID_ARGUMENT", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildRawErrorResponse(
                "Invalid request body. Verify JSON format and field data types",
                "SELLER_INVALID_BODY",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildRawErrorResponse(
                "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue(),
                "SELLER_INVALID_FORMAT",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rawMessage = ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        String normalized = rawMessage == null ? "" : rawMessage.toLowerCase();

        if (normalized.contains("email")) {
            return buildRawErrorResponse(
                    "A seller with the same email already exists",
                    "SELLER_DUPLICATE_EMAIL",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (normalized.contains("identification_number")) {
            return buildRawErrorResponse(
                    "A seller with the same identification number already exists",
                    "SELLER_DUPLICATE_IDENTIFICATION",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (normalized.contains("location")) {
            return buildRawErrorResponse(
                    "The provided locationId does not exist or is not valid",
                    "SELLER_INVALID_LOCATION",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (normalized.contains("document_type")) {
            return buildRawErrorResponse(
                    "The provided documentTypeId does not exist or is not valid",
                    "SELLER_INVALID_DOCUMENT_TYPE",
                    HttpStatus.BAD_REQUEST
            );
        }

        return buildRawErrorResponse(
                "The request violates a database data integrity rule",
                "SELLER_DATA_INTEGRITY_ERROR",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildRawErrorResponse(
                "Method '" + ex.getMethod() + "' is not supported for this route",
                "SELLER_METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        return buildRawErrorResponse(
                "Route '" + ex.getResourcePath() + "' does not exist",
                "SELLER_ROUTE_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildRawErrorResponse(
                "An unexpected error occurred, please try again",
                "INTERNAL_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(SellerApiException ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_MESSAGE, ex.getMessage());
        body.put(KEY_ERROR_CODE, ex.getErrorCode());
        body.put(KEY_TIMESTAMP, ex.getTimestamp());
        body.put(KEY_STATUS, status.value());
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildRawErrorResponse(String message,
                                                                      String errorCode,
                                                                      HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_MESSAGE, message);
        body.put(KEY_ERROR_CODE, errorCode);
        body.put(KEY_TIMESTAMP, LocalDateTime.now());
        body.put(KEY_STATUS, status.value());
        return ResponseEntity.status(status).body(body);
    }
}
