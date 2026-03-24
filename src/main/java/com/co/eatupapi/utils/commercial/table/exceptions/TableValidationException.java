package com.co.eatupapi.utils.commercial.table.exceptions;

public class TableValidationException extends TableApiException {

    public TableValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}