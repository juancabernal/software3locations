package com.co.eatupapi.utils.inventory.transfer.exceptions;

public class TransferNotFoundException extends TransferApiException {

    public TransferNotFoundException(String message) {
        super(message, "TRANSFER_NOT_FOUND");
    }
}