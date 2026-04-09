package com.co.eatupapi.utils.user.exceptions;

public class UserAuthenticationException extends UserApiException {
    public UserAuthenticationException(String message) {
        super(message, "USER_AUTHENTICATION_ERROR");
    }
}
