package com.co.eatupapi.utils.inventory.transfer.exceptions;

public class TransferValidationException extends TransferApiException {

    public TransferValidationException(String message) {
        super(message, "TRANSFER_VALIDATION_ERROR");
    }
}