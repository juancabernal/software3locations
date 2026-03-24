package com.co.eatupapi.repositories.commercial.taxRegime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeDomain;
import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRegimeRepository extends JpaRepository<TaxRegimeDomain, UUID> {

    List<TaxRegimeDomain> findByStatus(TaxRegimeStatus status);

    Optional<TaxRegimeDomain> findByName(String name);
}
