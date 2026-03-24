package com.co.eatupapi.utils.payment.cashreceipt.exceptions;

public class CashReceiptBusinessException extends CashReceiptException {

    public CashReceiptBusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}