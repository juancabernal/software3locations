package com.co.eatupapi.utils.commercial.sales.mapper;

import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    public SaleResponseDTO toDto(SaleDomain domain) {
        if (domain == null) {
            return null;
        }

        SaleResponseDTO dto = new SaleResponseDTO();
        dto.setId(domain.getId());
        dto.setSellerId(domain.getSellerId());
        dto.setSellerName(null);
        dto.setLocationId(domain.getLocationId());
        dto.setLocationName(null);
        dto.setTableId(domain.getTableId());
        dto.setStatus(domain.getStatus());
        dto.setTotalAmount(domain.getTotalAmount());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setModifiedDate(domain.getModifiedDate());

        if (domain.getDetails() != null) {
            dto.setDetails(domain.getDetails().stream().map(this::toDetailDto).toList());
        }

        return dto;
    }

    private SaleDetailDTO toDetailDto(SaleDetailDomain detail) {
        if (detail == null) {
            return null;
        }

        SaleDetailDTO dto = new SaleDetailDTO();
        dto.setRecipeId(detail.getRecipeId());
        dto.setLineDisplayName(detail.getLineDisplayName());
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setSubtotal(detail.getSubtotal());
        dto.setRecipeLineComment(detail.getRecipeLineComment());
        return dto;
    }
}
