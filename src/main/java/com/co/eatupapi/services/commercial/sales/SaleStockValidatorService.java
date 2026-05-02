package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import java.util.List;

public interface SaleStockValidatorService {

    void validateStockForSaleDetails(List<SaleDetailDTO> details);
}