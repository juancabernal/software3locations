package com.co.eatupapi.utils.commercial.purchase.exceptions;

public class PurchaseBusinessException extends PurchaseException {

    public PurchaseBusinessException(PurchaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}