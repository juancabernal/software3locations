package com.co.eatupapi.controllers.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.SalePatchDTO;
import com.co.eatupapi.dto.commercial.sales.SaleRequestDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.services.commercial.sales.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/commercial/api/v1/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> findAll() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> update(@PathVariable UUID id, @RequestBody SaleRequestDTO request) {
        return ResponseEntity.ok(saleService.updateSale(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> patch(@PathVariable UUID id, @RequestBody SalePatchDTO request) {
        return ResponseEntity.ok(saleService.patchSale(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
