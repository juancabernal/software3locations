package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.CustomerDiscountDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerDiscountRepository extends JpaRepository<CustomerDiscountDomain, UUID> {

    List<CustomerDiscountDomain> findByCustomerId(UUID customerId);
}
