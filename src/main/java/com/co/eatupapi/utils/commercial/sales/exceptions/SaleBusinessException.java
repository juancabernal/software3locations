package com.co.eatupapi.utils.commercial.sales.exceptions;

public class SaleBusinessException extends SaleApiException {
    public SaleBusinessException(String message) {
        super(message, "SALE_BUSINESS_ERROR");
    }
}
