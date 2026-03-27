package com.co.eatupapi.utils.commercial.sales.mapper;

import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class SaleMapper {

    public SaleResponseDTO toDto(SaleDomain domain) {
        if (domain == null) return null;

        SaleResponseDTO dto = new SaleResponseDTO();
        dto.setId(domain.getId());
        dto.setSellerId(domain.getSeller().getId());
        dto.setSellerName(domain.getSeller().getFirstName() + " " + domain.getSeller().getLastName());
        dto.setTableId(domain.getTableId());
        dto.setStatus(domain.getStatus());
        dto.setTotalAmount(domain.getTotalAmount());
        dto.setCreatedDate(domain.getCreatedDate());

        if (domain.getDetails() != null) {
            dto.setDetails(domain.getDetails().stream()
                    .map(this::toDetailDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public SaleDetailDTO toDetailDto(SaleDetailDomain detail) {
        if (detail == null) return null;

        SaleDetailDTO dto = new SaleDetailDTO();
        dto.setProductId(detail.getProduct().getId());
        dto.setProductName(detail.getProduct().getName());
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setSubtotal(detail.getSubtotal());
        return dto;
    }
}
