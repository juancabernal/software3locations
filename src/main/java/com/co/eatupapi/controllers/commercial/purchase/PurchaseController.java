package com.co.eatupapi.controllers.commercial.purchase;

import com.co.eatupapi.dto.commercial.purchase.PurchaseDTO;
import com.co.eatupapi.dto.commercial.purchase.PurchaseStatusUpdateDTO;
import com.co.eatupapi.services.commercial.purchase.PurchaseService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commercial/api/v1/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Se inyecta la interfaz PurchaseService, no la implementación concreta.
     * Spring resuelve automáticamente PurchaseServiceImpl gracias a @Service.
     */
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseService.createPurchase(request));
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable String purchaseId) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(purchaseId));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getPurchases(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(purchaseService.getPurchases(status));
    }

    @PutMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDTO> updatePurchase(
            @PathVariable String purchaseId,
            @RequestBody PurchaseDTO request) {
        return ResponseEntity.ok(purchaseService.updatePurchase(purchaseId, request));
    }

    @PatchMapping("/{purchaseId}/status")
    public ResponseEntity<PurchaseDTO> updateStatus(
            @PathVariable String purchaseId,
            @RequestBody PurchaseStatusUpdateDTO request) {
        return ResponseEntity.ok(purchaseService.updateStatus(purchaseId, request.getStatus()));
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> deletePurchase(@PathVariable String purchaseId) {
        purchaseService.deletePurchase(purchaseId);
        return ResponseEntity.noContent().build();
    }
}