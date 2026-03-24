package com.co.eatupapi.utils.commercial.table.exceptions;

import java.time.LocalDateTime;

public abstract class TableApiException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    protected TableApiException(String message, String errorCode) {
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