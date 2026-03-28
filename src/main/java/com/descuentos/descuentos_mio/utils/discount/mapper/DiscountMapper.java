package com.descuentos.descuentos_mio.utils.discount.mapper;

import com.descuentos.descuentos_mio.domain.DiscountDomain;
import com.descuentos.descuentos_mio.dto.DiscountDTO;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {

    public DiscountDTO toDto(DiscountDomain domain) {
        return new DiscountDTO(
                domain.getId(),
                domain.getCategoryId(),
                domain.getPercentage(),
                domain.getDescription(),
                domain.getStatus()
        );
    }

    public DiscountDomain toDomain(DiscountDTO dto) {
        return new DiscountDomain(
                dto.getId(),
                dto.getCategoryId(),
                dto.getPercentage(),
                dto.getDescription(),
                dto.getStatus()
        );
    }

    public void updateDomain(DiscountDomain domain, DiscountDTO dto) {
        domain.setCategoryId(dto.getCategoryId());
        domain.setPercentage(dto.getPercentage());
        domain.setDescription(dto.getDescription());
        domain.setStatus(dto.getStatus());
    }
}
