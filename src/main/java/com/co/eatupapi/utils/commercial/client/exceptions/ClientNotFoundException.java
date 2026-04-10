package com.co.eatupapi.utils.commercial.client.exceptions;

public class ClientNotFoundException extends ApiException {
    public ClientNotFoundException(String message) {
        super(message, "CLIENT_NOT_FOUND");
    }
}