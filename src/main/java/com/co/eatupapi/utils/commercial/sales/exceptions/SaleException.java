package com.co.eatupapi.utils.commercial.sales.exceptions;

public abstract class SaleException extends RuntimeException {

    private final SaleErrorCode errorCode;

    protected SaleException(SaleErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SaleErrorCode getErrorCode() {
        return errorCode;
    }
}
