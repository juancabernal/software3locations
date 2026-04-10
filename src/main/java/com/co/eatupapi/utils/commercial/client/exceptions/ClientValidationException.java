package com.co.eatupapi.utils.commercial.client.exceptions;

public class ClientValidationException extends ApiException {
    public ClientValidationException(String message) {
        super(message, "INVALID_CLIENT_DATA");
    }
}