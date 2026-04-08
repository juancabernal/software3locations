package com.co.eatupapi.services.payment.paymentmethod;

import com.co.eatupapi.dto.payment.paymentmethod.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethodResponse> getActivePaymentMethods();

    List<PaymentMethodResponse> getAllPaymentMethods();
}
