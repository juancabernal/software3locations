package com.co.eatupapi.repositories.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<SaleDomain, UUID> {
}
