package com.co.eatupapi.services.payment.paymentmethod;

import com.co.eatupapi.dto.payment.paymentmethod.PaymentMethodResponse;
import com.co.eatupapi.repositories.payment.paymentmethod.PaymentMethodRepository;
import com.co.eatupapi.utils.payment.paymentmethod.mapper.PaymentMethodMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository,
                                    PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @Override
    public List<PaymentMethodResponse> getActivePaymentMethods() {
        return paymentMethodRepository.findByActiveTrue()
                .stream()
                .map(paymentMethodMapper::toResponse)
                .toList();
    }

    @Override
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(paymentMethodMapper::toResponse)
                .toList();
    }
}
