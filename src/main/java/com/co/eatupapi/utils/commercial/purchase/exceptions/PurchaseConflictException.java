package com.co.eatupapi.utils.commercial.purchase.exceptions;

public class PurchaseConflictException extends PurchaseException {

    public PurchaseConflictException(PurchaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}