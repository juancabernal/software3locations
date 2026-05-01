package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceStatus;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import java.util.List;
import java.util.UUID;

public interface RecipePreparationTraceService {
    void createInitialTraces(SaleDomain sale);
    void deleteTracesBySaleId(UUID saleId);
    List<RecipePreparationTraceResponseDTO> findBySaleId(UUID saleId);
    void updateTraceStatus(UUID saleDetailId, RecipePreparationTraceStatus newStatus, String observation);
}
