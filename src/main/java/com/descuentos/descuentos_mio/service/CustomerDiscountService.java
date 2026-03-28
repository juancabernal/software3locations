package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.dto.CustomerDiscountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerDiscountService {

    List<CustomerDiscountDTO> getAllCustomerDiscounts();

    List<CustomerDiscountDTO> getDiscountsByCustomerId(UUID customerId);

    CustomerDiscountDTO createCustomerDiscount(CustomerDiscountDTO customerDiscount);

    Optional<CustomerDiscountDTO> updateCustomerDiscount(UUID id, CustomerDiscountDTO customerDiscount);

    boolean deleteCustomerDiscount(UUID id);
}
