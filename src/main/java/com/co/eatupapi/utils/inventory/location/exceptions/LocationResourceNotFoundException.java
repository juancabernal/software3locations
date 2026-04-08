package com.co.eatupapi.utils.inventory.location.exceptions;

public class LocationResourceNotFoundException extends LocationApiException {

    public LocationResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
