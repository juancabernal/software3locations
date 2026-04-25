package com.co.eatupapi.utils.commercial.sales.exceptions;

public class SaleValidationException extends SaleException {
    public SaleValidationException(String message) {
        super(SaleErrorCode.VALIDATION_ERROR, message);
    }
}
