package com.co.eatupapi.repositories.payment.paymentmethod;

import com.co.eatupapi.domain.payment.paymentmethod.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {

    List<PaymentMethod> findByActiveTrue();
}