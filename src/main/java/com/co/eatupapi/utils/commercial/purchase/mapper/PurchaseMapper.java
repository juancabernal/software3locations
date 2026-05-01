package com.co.eatupapi.utils.commercial.purchase.mapper;

import com.co.eatupapi.domain.commercial.purchase.PurchaseDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseItemDomain;
import com.co.eatupapi.dto.commercial.purchase.CreatePurchaseItemRequest;
import com.co.eatupapi.dto.commercial.purchase.PurchaseItemResponse;
import com.co.eatupapi.dto.commercial.purchase.PurchaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(target = "items", source = "items")
    PurchaseResponse toResponse(PurchaseDomain domain);

    PurchaseItemResponse toItemResponse(PurchaseItemDomain domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    PurchaseItemDomain toItemDomain(CreatePurchaseItemRequest request);
}