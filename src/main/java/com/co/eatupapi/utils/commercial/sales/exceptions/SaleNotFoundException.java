package com.co.eatupapi.utils.commercial.sales.exceptions;

public class SaleNotFoundException extends SaleApiException {
    public SaleNotFoundException(String message) {
        super(message, "SALE_NOT_FOUND");
    }
}
