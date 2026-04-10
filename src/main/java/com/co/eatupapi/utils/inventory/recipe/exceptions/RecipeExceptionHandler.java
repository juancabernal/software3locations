package com.co.eatupapi.utils.inventory.recipe.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.co.eatupapi.controllers.inventory.recipe")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RecipeExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RecipeNotFoundException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                ex.getErrorCode(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RecipeBusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(RecipeBusinessException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                ex.getErrorCode(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message;
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        } else if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            message = ex.getBindingResult().getGlobalErrors().get(0).getDefaultMessage();
        } else {
            message = "Validation failed";
        }

        ApiErrorResponse error = new ApiErrorResponse(
                ErrorCode.VALIDATION_ERROR,
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}