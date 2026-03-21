package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.CustomerDiscountDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerDiscountRepository {

    List<CustomerDiscountDomain> findAll();

    List<CustomerDiscountDomain> findByCustomerId(UUID customerId);

    Optional<CustomerDiscountDomain> findById(UUID id);

    CustomerDiscountDomain save(CustomerDiscountDomain customerDiscount);

    Optional<CustomerDiscountDomain> update(UUID id, CustomerDiscountDomain customerDiscount);

    boolean deleteById(UUID id);
}
