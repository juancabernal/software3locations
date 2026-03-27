package com.co.eatupapi.dto.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleStatus;
import java.util.List;

public record SalePatchDTO(
        SaleStatus status,
        String sellerId,
        String tableId,
        List<SaleDetailDTO> details
) {}
