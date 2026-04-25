package com.co.eatupapi.utils.commercial.purchase.exceptions;

public class PurchaseNotFoundException extends PurchaseException {

    public PurchaseNotFoundException(String message) {
        super(PurchaseErrorCode.PURCHASE_NOT_FOUND, message);
    }
}