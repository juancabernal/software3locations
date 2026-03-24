package com.co.eatupapi.repositories.commercial.taxRegime.impl;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeDomain;
import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeStatus;
import com.co.eatupapi.repositories.commercial.taxRegime.TaxRegimeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TaxRegimeRepositoryImpl implements TaxRegimeRepository {

    private final Map<String, TaxRegimeDomain> taxRegimes = new LinkedHashMap<>();

    @Override
    public void initializeData(List<TaxRegimeDomain> initialTaxRegimes) {
        taxRegimes.clear();
        for (TaxRegimeDomain taxRegime : initialTaxRegimes) {
            taxRegimes.put(taxRegime.getId(), taxRegime);
        }
    }

    @Override
    public TaxRegimeDomain save(TaxRegimeDomain taxRegime) {
        taxRegimes.put(taxRegime.getId(), taxRegime);
        return taxRegime;
    }

    @Override
    public Optional<TaxRegimeDomain> findById(String id) {
        return Optional.ofNullable(taxRegimes.get(id));
    }

    @Override
    public List<TaxRegimeDomain> findAll() {
        return new ArrayList<>(taxRegimes.values());
    }

    @Override
    public List<TaxRegimeDomain> findByStatus(TaxRegimeStatus status) {
        List<TaxRegimeDomain> result = new ArrayList<>();
        for (TaxRegimeDomain taxRegime : taxRegimes.values()) {
            if (taxRegime.getStatus() == status) {
                result.add(taxRegime);
            }
        }
        return result;
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        for (TaxRegimeDomain taxRegime : taxRegimes.values()) {
            if (taxRegime.getName() != null && taxRegime.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByNameIgnoreCaseAndIdNot(String name, String id) {
        for (TaxRegimeDomain taxRegime : taxRegimes.values()) {
            if (taxRegime.getName() != null
                    && taxRegime.getName().equalsIgnoreCase(name)
                    && !taxRegime.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
