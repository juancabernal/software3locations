package com.co.eatupapi.utils.commercial.seller.exceptions;

public class SellerValidationException extends SellerApiException {
    public SellerValidationException(String message) {
        super(message, "SELLER_VALIDATION_ERROR");
    }
}