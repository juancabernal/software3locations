package com.co.eatupapi.utils.user.exceptions;

import java.time.LocalDateTime;

public abstract class UserApiException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected UserApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
