package com.co.eatupapi.utils.commercial.provider.exceptions;

import java.time.LocalDateTime;

public abstract class ApiException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected ApiException(String message, String errorCode) {
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
