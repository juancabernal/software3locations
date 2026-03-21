package com.co.eatupapi.utils.inventory.product.exceptions;

public class BusinessException extends ApiException {

    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}