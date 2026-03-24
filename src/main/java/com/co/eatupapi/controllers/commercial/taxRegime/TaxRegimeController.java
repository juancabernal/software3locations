package com.co.eatupapi.controllers.commercial.taxRegime;

import java.util.List;

import com.co.eatupapi.dto.commercial.taxRegime.TaxRegimeDTO;
import com.co.eatupapi.dto.commercial.taxRegime.TaxRegimeStatusUpdateDTO;
import com.co.eatupapi.services.commercial.taxRegime.TaxRegimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commercial/api/v1/tax-regimes")
public class TaxRegimeController {

    private final TaxRegimeService taxRegimeService;

    public TaxRegimeController(TaxRegimeService taxRegimeService) {
        this.taxRegimeService = taxRegimeService;
    }

    @PostMapping
    public ResponseEntity<TaxRegimeDTO> createTaxRegime(@RequestBody TaxRegimeDTO request) {
        TaxRegimeDTO saved = taxRegimeService.createTaxRegime(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{taxRegimeId}")
    public ResponseEntity<TaxRegimeDTO> getTaxRegimeById(@PathVariable String taxRegimeId) {
        TaxRegimeDTO taxRegime = taxRegimeService.getTaxRegimeById(taxRegimeId);
        return ResponseEntity.ok(taxRegime);
    }

    @GetMapping
    public ResponseEntity<List<TaxRegimeDTO>> getTaxRegimes(@RequestParam(required = false) String status) {
        List<TaxRegimeDTO> taxRegimes = taxRegimeService.getTaxRegimes(status);
        return ResponseEntity.ok(taxRegimes);
    }

    @PutMapping("/{taxRegimeId}")
    public ResponseEntity<TaxRegimeDTO> updateTaxRegime(@PathVariable String taxRegimeId,
                                                        @RequestBody TaxRegimeDTO request) {
        TaxRegimeDTO updated = taxRegimeService.updateTaxRegime(taxRegimeId, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{taxRegimeId}/status")
    public ResponseEntity<TaxRegimeDTO> changeStatus(@PathVariable String taxRegimeId,
                                                     @RequestBody TaxRegimeStatusUpdateDTO request) {
        TaxRegimeDTO updated = taxRegimeService.changeStatus(taxRegimeId, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}
