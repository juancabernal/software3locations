package com.co.eatupapi.utils.user.exceptions;

public class UserValidationException extends UserApiException {
    public UserValidationException(String message) {
        super(message, "USER_VALIDATION_ERROR");
    }
}
