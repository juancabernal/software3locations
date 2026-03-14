package com.co.eatupapi.utils.commercial.provider.exceptions;

public class BusinessException extends ApiException {

    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}
