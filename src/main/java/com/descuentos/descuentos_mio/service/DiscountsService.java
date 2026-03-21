package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.dto.DiscountsDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountsService {

    List<DiscountsDto> getAllDiscounts();

    Optional<DiscountsDto> getDiscountById(UUID id);

    DiscountsDto createDiscount(DiscountsDto discount);

    Optional<DiscountsDto> updateDiscount(UUID id, DiscountsDto discount);

    Optional<DiscountsDto> updateDiscountStatus(UUID id, Boolean status);

    boolean deleteDiscount(UUID id);
}
