package com.co.eatupapi.utils.commercial.purchase.exceptions;

public abstract class PurchaseException extends RuntimeException {

    private final PurchaseErrorCode errorCode;

    protected PurchaseException(PurchaseErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PurchaseErrorCode getErrorCode() {
        return errorCode;
    }
}
