package com.co.eatupapi.repositories.commercial.taxRegime;
import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeDomain;
import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeStatus;

import java.util.List;
import java.util.Optional;

public interface TaxRegimeRepository {

    void initializeData(List<TaxRegimeDomain> taxRegimes);

    TaxRegimeDomain save(TaxRegimeDomain taxRegimeDomain);

    Optional<TaxRegimeDomain> findById(String id);

    List<TaxRegimeDomain> findAll();

    List<TaxRegimeDomain> findByStatus(TaxRegimeStatus status);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, String id);
}
