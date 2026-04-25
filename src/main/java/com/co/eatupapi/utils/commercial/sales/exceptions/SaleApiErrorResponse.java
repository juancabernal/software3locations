package com.co.eatupapi.utils.commercial.sales.exceptions;

import java.time.LocalDateTime;

public class SaleApiErrorResponse {

    private String error;
    private String message;
    private LocalDateTime timestamp;

    public SaleApiErrorResponse(SaleErrorCode error, String message) {
        this.error = error.name();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
