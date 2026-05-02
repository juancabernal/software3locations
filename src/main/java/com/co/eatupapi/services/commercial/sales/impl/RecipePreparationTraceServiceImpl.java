package com.co.eatupapi.services.commercial.sales.impl;

import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceDomain;
import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceStatus;
import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import com.co.eatupapi.repositories.commercial.sales.RecipePreparationTraceRepository;
import com.co.eatupapi.services.commercial.sales.RecipePreparationTraceService;
import com.co.eatupapi.utils.commercial.sales.mapper.RecipePreparationTraceMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipePreparationTraceServiceImpl implements RecipePreparationTraceService {

    private final RecipePreparationTraceRepository traceRepository;
    private final RecipePreparationTraceMapper traceMapper;

    public RecipePreparationTraceServiceImpl(RecipePreparationTraceRepository traceRepository,
                                             RecipePreparationTraceMapper traceMapper) {
        this.traceRepository = traceRepository;
        this.traceMapper = traceMapper;
    }

    @Override
    @Transactional
    public void createInitialTraces(SaleDomain sale) {
        if (sale == null || sale.getDetails() == null || sale.getDetails().isEmpty()) {
            return;
        }

        List<RecipePreparationTraceDomain> traces = sale.getDetails().stream().map(detail -> buildInitialTrace(sale, detail)).toList();
        traceRepository.saveAll(traces);
    }

    @Override
    @Transactional
    public void deleteTracesBySaleId(UUID saleId) {
        traceRepository.deleteBySale_Id(saleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreparationTraceResponseDTO> findBySaleId(UUID saleId) {
        return traceRepository.findBySale_Id(saleId).stream().map(traceMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void updateTraceStatus(UUID saleDetailId, RecipePreparationTraceStatus newStatus, String observation) {
        List<RecipePreparationTraceDomain> traces = traceRepository.findBySaleDetail_Id(saleDetailId);
        for (RecipePreparationTraceDomain trace : traces) {
            trace.setStatus(newStatus);
            trace.setObservation(observation);
        }
        traceRepository.saveAll(traces);
    }

    private RecipePreparationTraceDomain buildInitialTrace(SaleDomain sale, SaleDetailDomain detail) {
        RecipePreparationTraceDomain trace = new RecipePreparationTraceDomain();
        trace.setSale(sale);
        trace.setSaleDetail(detail);
        trace.setRecipeId(detail.getRecipeId());
        trace.setStatus(RecipePreparationTraceStatus.PENDING_STOCK_DISCOUNT);
        trace.setObservation(null);
        return trace;
    }
}
