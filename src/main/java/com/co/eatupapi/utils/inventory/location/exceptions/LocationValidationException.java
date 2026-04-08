package com.co.eatupapi.utils.inventory.location.exceptions;

public class LocationValidationException extends LocationApiException {

    public LocationValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
