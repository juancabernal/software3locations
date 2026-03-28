package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.DiscountDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<DiscountDomain, UUID> {

    List<DiscountDomain> findByStatus(Boolean status);
}
