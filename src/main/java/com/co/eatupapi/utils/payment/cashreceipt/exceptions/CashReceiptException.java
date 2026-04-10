package com.co.eatupapi.utils.payment.cashreceipt.exceptions;

public abstract class CashReceiptException extends RuntimeException {

    private final ErrorCode errorCode;

    protected CashReceiptException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}