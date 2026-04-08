package com.co.eatupapi.utils.user.exceptions;

public class UserNotFoundException extends UserApiException {
    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND");
    }
}
