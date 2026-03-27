package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.DiscountsDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiscountsRepository extends JpaRepository<DiscountsDomain, UUID> {

    List<DiscountsDomain> findByStatus(Boolean status);
}
