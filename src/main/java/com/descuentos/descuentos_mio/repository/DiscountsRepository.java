package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.DiscountsDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountsRepository {

    List<DiscountsDomain> findAll();

    Optional<DiscountsDomain> findById(UUID id);

    DiscountsDomain save(DiscountsDomain discount);

    Optional<DiscountsDomain> update(UUID id, DiscountsDomain discount);
}
