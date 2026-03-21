package com.co.eatupapi.utils.commercial.table.exceptions;

public class TableBusinessException extends TableApiException {

    public TableBusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }
}