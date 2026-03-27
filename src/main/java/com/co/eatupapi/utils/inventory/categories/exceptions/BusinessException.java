package com.co.eatupapi.utils.inventory.categories.exceptions;

public class BusinessException extends ApiException {

    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}
