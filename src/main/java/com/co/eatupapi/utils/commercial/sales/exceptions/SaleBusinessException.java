package com.co.eatupapi.utils.commercial.sales.exceptions;

public class SaleBusinessException extends SaleException {
    public SaleBusinessException(String message) {
        super(SaleErrorCode.BUSINESS_RULE_VIOLATION, message);
    }
}
