package com.co.eatupapi.utils.payment.cashreceipt.exceptions;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(ErrorCode error, String message) {
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