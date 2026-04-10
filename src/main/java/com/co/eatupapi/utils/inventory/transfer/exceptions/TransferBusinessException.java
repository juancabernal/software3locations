package com.co.eatupapi.utils.inventory.transfer.exceptions;

public class TransferBusinessException extends TransferApiException {

    public TransferBusinessException(String message) {
        super(message, "TRANSFER_BUSINESS_ERROR");
    }
}