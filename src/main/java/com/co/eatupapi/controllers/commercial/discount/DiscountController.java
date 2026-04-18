package com.co.eatupapi.controllers.commercial.discount;

import com.co.eatupapi.dto.commercial.discount.DiscountDTO;
import com.co.eatupapi.services.commercial.discount.DiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/comercial/api/v1/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public List<DiscountDTO> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountDTO> getDiscountById(@PathVariable UUID discountId) {
        return discountService.getDiscountById(discountId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public List<DiscountDTO> getActiveDiscounts() {
        return discountService.getActiveDiscounts();
    }


    @PostMapping
    public ResponseEntity<Map<String, String>> createDiscount(@RequestBody DiscountDTO discountDto) {
        discountService.createDiscount(discountDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Descuento creado con éxito"));
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<DiscountDTO> updateDiscount(
            @PathVariable UUID discountId,
            @RequestBody DiscountDTO discountDto
    ) {
        return discountService.updateDiscount(discountId, discountDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{discountId}/status")
    public ResponseEntity<DiscountDTO> updateDiscountStatus(
            @PathVariable UUID discountId,
            @RequestBody Map<String, Boolean> request
    ) {
        return discountService.updateDiscountStatus(discountId, request.get("status"))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable UUID discountId) {
        if (discountService.deleteDiscount(discountId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

