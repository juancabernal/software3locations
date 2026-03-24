package com.co.eatupapi.utils.payment.invoice.exceptions;

public class InvoiceValidationException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvoiceValidationException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_REQUEST;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}