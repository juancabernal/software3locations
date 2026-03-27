package com.co.eatupapi.utils.payment.paymentmethod.mapper;

import com.co.eatupapi.domain.payment.paymentmethod.PaymentMethod;
import com.co.eatupapi.dto.payment.paymentmethod.PaymentMethodResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodResponse toResponse(PaymentMethod paymentMethod);
}
