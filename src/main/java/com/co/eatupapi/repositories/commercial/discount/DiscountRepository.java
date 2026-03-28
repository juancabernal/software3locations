package com.co.eatupapi.repositories.commercial.discount;

import com.co.eatupapi.domain.commercial.discount.DiscountDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<DiscountDomain, UUID> {

    List<DiscountDomain> findByStatus(Boolean status);
}