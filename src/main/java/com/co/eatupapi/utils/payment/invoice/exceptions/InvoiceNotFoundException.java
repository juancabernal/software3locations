package com.co.eatupapi.utils.payment.invoice.exceptions;

public class InvoiceNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvoiceNotFoundException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVOICE_NOT_FOUND;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}