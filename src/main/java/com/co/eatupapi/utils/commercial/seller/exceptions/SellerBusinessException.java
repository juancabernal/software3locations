package com.co.eatupapi.utils.commercial.seller.exceptions;

public class SellerBusinessException extends SellerApiException {
    public SellerBusinessException(String message) {
        super(message, "SELLER_BUSINESS_ERROR");
    }
}