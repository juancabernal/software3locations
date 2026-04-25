package com.co.eatupapi.utils.commercial.sales.exceptions;

public class SaleNotFoundException extends SaleException {

    public SaleNotFoundException(String message) {
        super(SaleErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
