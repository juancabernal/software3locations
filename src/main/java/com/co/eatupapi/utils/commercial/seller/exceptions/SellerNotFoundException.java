package com.co.eatupapi.utils.commercial.seller.exceptions;

public class SellerNotFoundException extends SellerApiException {
    public SellerNotFoundException(String message) {
        super(message, "SELLER_NOT_FOUND");
    }
}