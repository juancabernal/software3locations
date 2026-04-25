package com.co.eatupapi.utils.commercial.sales.exceptions;

import java.time.LocalDateTime;

public abstract class SaleException extends RuntimeException {

    private final SaleErrorCode errorCode;
    private final LocalDateTime timestamp;

    protected SaleException(SaleErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public SaleErrorCode getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
