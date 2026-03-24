package com.co.eatupapi.utils.payment.invoice.exceptions;

public class InvoiceBusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvoiceBusinessException(String message) {
        super(message);
        this.errorCode = ErrorCode.BUSINESS_RULE_VIOLATION;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}