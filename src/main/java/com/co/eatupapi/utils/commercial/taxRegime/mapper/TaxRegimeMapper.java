package com.co.eatupapi.utils.commercial.taxRegime.mapper;

import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeDomain;
import com.co.eatupapi.dto.commercial.taxRegime.TaxRegimeDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxRegimeMapper {

    public TaxRegimeDTO toDTO(TaxRegimeDomain taxRegime) {
        TaxRegimeDTO dto = new TaxRegimeDTO();
        dto.setId(taxRegime.getId());
        dto.setName(taxRegime.getName());
        dto.setStatus(taxRegime.getStatus());
        return dto;
    }

    public TaxRegimeDomain toDomain(TaxRegimeDTO dto) {
        TaxRegimeDomain taxRegime = new TaxRegimeDomain();
        updateDomain(taxRegime, dto);
        taxRegime.setId(dto.getId());
        return taxRegime;
    }

    public void updateDomain(TaxRegimeDomain taxRegime, TaxRegimeDTO dto) {
        taxRegime.setName(dto.getName() == null ? null : dto.getName().trim());
        taxRegime.setStatus(dto.getStatus());
    }
}
