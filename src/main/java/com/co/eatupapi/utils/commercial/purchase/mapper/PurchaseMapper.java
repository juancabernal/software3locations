package com.co.eatupapi.utils.commercial.purchase.mapper;

import com.co.eatupapi.domain.commercial.purchase.PurchaseDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseItemDomain;
import com.co.eatupapi.dto.commercial.purchase.PurchaseDTO;
import com.co.eatupapi.dto.commercial.purchase.PurchaseItemDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    // ── Domain → DTO ───────────────────────────────────────────────────────────

    public PurchaseDTO toDto(PurchaseDomain domain) {
        PurchaseDTO dto = new PurchaseDTO();

        dto.setId(domain.getId());
        dto.setOrderNumber(domain.getOrderNumber());
        dto.setBranchId(domain.getBranchId());
        dto.setTotal(domain.getTotal());
        dto.setStatus(domain.getStatus());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setModifiedDate(domain.getModifiedDate());

        if (domain.getProvider() != null) {
            dto.setProviderId(domain.getProvider().getId().toString());
            dto.setProviderName(domain.getProvider().getBusinessName());
        }

        dto.setItems(toItemDtoList(domain.getItems()));
        return dto;
    }

    // ── DTO → Domain ───────────────────────────────────────────────────────────


    public List<PurchaseItemDomain> toItemDomainList(List<PurchaseItemDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }

        List<PurchaseItemDomain> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (PurchaseItemDTO dto : dtos) {
            PurchaseItemDomain item = new PurchaseItemDomain();
            item.setId(UUID.randomUUID().toString());   // ID generado aquí, no en BD
            item.setProductId(dto.getProductId());
            item.setProductName(dto.getProductName());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            // subtotal se recalcula internamente al setear quantity y unitPrice
            item.setCreatedDate(now);
            item.setModifiedDate(now);
            list.add(item);
        }
        return list;
    }

    // ── Helpers privados ───────────────────────────────────────────────────────

    private List<PurchaseItemDTO> toItemDtoList(List<PurchaseItemDomain> items) {
        if (items == null) {
            return new ArrayList<>();
        }

        List<PurchaseItemDTO> list = new ArrayList<>();
        for (PurchaseItemDomain item : items) {
            PurchaseItemDTO dto = new PurchaseItemDTO();
            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setSubtotal(item.getSubtotal());
            list.add(dto);
        }
        return list;
    }
}