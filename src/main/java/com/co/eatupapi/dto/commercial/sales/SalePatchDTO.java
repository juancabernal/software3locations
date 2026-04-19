package com.co.eatupapi.dto.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleStatus;
import java.util.List;
import java.util.UUID;

public record SalePatchDTO(
        SaleStatus status,
        String sellerId,
        UUID locationId,
        String tableId,
        List<SaleDetailDTO> details
) {}
