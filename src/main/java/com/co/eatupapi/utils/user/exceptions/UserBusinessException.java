package com.co.eatupapi.utils.user.exceptions;

public class UserBusinessException extends UserApiException {
    public UserBusinessException(String message) {
        super(message, "USER_BUSINESS_ERROR");
    }
}
