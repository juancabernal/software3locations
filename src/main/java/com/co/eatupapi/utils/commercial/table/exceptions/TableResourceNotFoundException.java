package com.co.eatupapi.utils.commercial.table.exceptions;

public class TableResourceNotFoundException extends TableApiException {

    public TableResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}