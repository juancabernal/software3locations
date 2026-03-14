package com.co.eatupapi.utils.commercial.provider.exceptions;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
