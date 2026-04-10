package com.co.eatupapi.utils.commercial.client.exceptions;

public class ClientBusinessException extends ApiException {

    public ClientBusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}