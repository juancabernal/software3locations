package com.co.eatupapi.utils.payment.cashreceipt.exceptions;

public class CashReceiptNotFoundException extends CashReceiptException {

  public CashReceiptNotFoundException(String message) {
    super(ErrorCode.CASH_RECEIPT_NOT_FOUND, message);
  }
}