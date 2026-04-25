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
        if (domain.getSeller() != null) {
            dto.setSellerId(domain.getSeller().getId().toString());
            dto.setSellerName(domain.getSeller().getFirstName() + " " + domain.getSeller().getLastName());
        } else {
            dto.setSellerId(null);
            dto.setSellerName(null);
        }
        if (domain.getLocation() != null) {
            dto.setLocationId(domain.getLocation().getId());
            dto.setLocationName(domain.getLocation().getName());
        } else {
            dto.setLocationId(null);
            dto.setLocationName(null);
        }
        dto.setTableId(domain.getTableId());
        dto.setStatus(domain.getStatus());
        dto.setTotalAmount(domain.getTotalAmount());
        dto.setCreatedDate(domain.getCreatedDate());

        if (domain.getDetails() != null) {
            dto.setDetails(domain.getDetails().stream()
                    .map(this::toDetailDto)
                    .toList());
        }

        return dto;
    }

    public SaleDetailDTO toDetailDto(SaleDetailDomain detail) {
        if (detail == null) {
            return null;
        }

        SaleDetailDTO dto = new SaleDetailDTO();
        if (detail.getRecipe() != null) {
            dto.setRecipeId(detail.getRecipe().getId());
            dto.setRecipeName(detail.getRecipe().getName());
        } else {
            dto.setRecipeId(null);
            dto.setRecipeName(detail.getLineDisplayName());
        }
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setSubtotal(detail.getSubtotal());
        dto.setRecipeComment(detail.getRecipeLineComment());
        return dto;
    }
}
