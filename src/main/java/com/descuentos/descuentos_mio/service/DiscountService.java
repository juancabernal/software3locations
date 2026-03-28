package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.dto.DiscountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountService {

    List<DiscountDTO> getAllDiscounts();

    Optional<DiscountDTO> getDiscountById(UUID id);

    DiscountDTO createDiscount(DiscountDTO discount);

    Optional<DiscountDTO> updateDiscount(UUID id, DiscountDTO discount);


    Optional<DiscountDTO> updateDiscountStatus(UUID id, Boolean status);

    boolean deleteDiscount(UUID id);
}
