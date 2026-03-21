package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.dto.CustomerDiscountDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerDiscountService {

    List<CustomerDiscountDto> getAllCustomerDiscounts();

    List<CustomerDiscountDto> getDiscountsByCustomerId(UUID customerId);

    CustomerDiscountDto createCustomerDiscount(CustomerDiscountDto customerDiscount);

    Optional<CustomerDiscountDto> updateCustomerDiscount(UUID id, CustomerDiscountDto customerDiscount);

    boolean deleteCustomerDiscount(UUID id);
}
