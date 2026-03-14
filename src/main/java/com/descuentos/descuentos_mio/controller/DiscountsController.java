package com.descuentos.descuentos_mio.controller;

import com.descuentos.descuentos_mio.dto.DiscountsDto;
import com.descuentos.descuentos_mio.service.DiscountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/comercial/api/v1/discounts")
public class DiscountsController {

    private final DiscountsService discountsService;

    public DiscountsController(DiscountsService discountsService) {
        this.discountsService = discountsService;
    }

    @GetMapping
    public List<DiscountsDto> getAllDiscounts() {
        return discountsService.getAllDiscounts();
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountsDto> getDiscountById(@PathVariable UUID discountId) {
        return discountsService.getDiscountById(discountId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DiscountsDto> createDiscount(@RequestBody DiscountsDto discountsDto) {
        DiscountsDto created = discountsService.createDiscount(discountsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<DiscountsDto> updateDiscount(
            @PathVariable UUID discountId,
            @RequestBody DiscountsDto discountsDto
    ) {
        return discountsService.updateDiscount(discountId, discountsDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
