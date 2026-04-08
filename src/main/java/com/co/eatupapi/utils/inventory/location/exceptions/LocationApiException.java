package com.co.eatupapi.utils.inventory.location.exceptions;

import java.time.LocalDateTime;

public abstract class LocationApiException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected LocationApiException(String message, String errorCode) {
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
