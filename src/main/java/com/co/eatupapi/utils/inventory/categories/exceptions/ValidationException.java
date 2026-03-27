package com.co.eatupapi.utils.inventory.categories.exceptions;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
