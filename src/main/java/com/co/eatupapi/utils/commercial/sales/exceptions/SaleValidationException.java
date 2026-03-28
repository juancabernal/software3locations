package com.co.eatupapi.utils.commercial.sales.exceptions;

public class SaleValidationException extends SaleApiException {
    public SaleValidationException(String message) {
        super(message, "SALE_VALIDATION_ERROR");
    }
}
