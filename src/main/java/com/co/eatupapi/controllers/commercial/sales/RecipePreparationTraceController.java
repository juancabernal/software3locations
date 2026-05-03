package com.co.eatupapi.controllers.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import com.co.eatupapi.services.commercial.sales.RecipePreparationTraceService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commercial/api/v1/sales")
public class RecipePreparationTraceController {

    private final RecipePreparationTraceService traceService;

    public RecipePreparationTraceController(RecipePreparationTraceService traceService) {
        this.traceService = traceService;
    }

    @GetMapping("/preparations")
    public ResponseEntity<List<RecipePreparationTraceResponseDTO>> findAll() {
        return ResponseEntity.ok(traceService.findAll());
    }

    @GetMapping("/{saleId}/preparations")
    public ResponseEntity<List<RecipePreparationTraceResponseDTO>> findBySaleId(@PathVariable UUID saleId) {
        return ResponseEntity.ok(traceService.findBySaleId(saleId));
    }

    @GetMapping("/preparations/{traceId}")
    public ResponseEntity<RecipePreparationTraceResponseDTO> findById(@PathVariable UUID traceId) {
        return ResponseEntity.ok(traceService.findById(traceId));
    }
}
