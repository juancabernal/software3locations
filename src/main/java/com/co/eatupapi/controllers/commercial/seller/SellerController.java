package com.co.eatupapi.controllers.commercial.seller;

import com.co.eatupapi.dto.commercial.seller.SellerDTO;
import com.co.eatupapi.dto.commercial.seller.SellerStatusUpdateDTO;
import com.co.eatupapi.services.commercial.seller.SellerService;
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
import com.co.eatupapi.dto.commercial.seller.SellerPatchDTO;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comercialapi/v1/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<SellerDTO> createSeller(@RequestBody SellerDTO request) {
        SellerDTO saved = sellerService.createSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<SellerDTO>> getSellers(@RequestParam(required = false) String status) {
        List<SellerDTO> sellers = sellerService.getSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerDTO> getSellerById(@PathVariable UUID sellerId) {
        SellerDTO seller = sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(seller);
    }

    @PutMapping("/{sellerId}")
    public ResponseEntity<SellerDTO> updateSeller(@PathVariable UUID sellerId,
                                                  @RequestBody SellerDTO request) {
        SellerDTO updated = sellerService.updateSeller(sellerId, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{sellerId}/status")
    public ResponseEntity<SellerDTO> updateStatus(@PathVariable UUID sellerId,
                                                  @RequestBody SellerStatusUpdateDTO request) {
        SellerDTO updated = sellerService.updateStatus(sellerId, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{sellerId}")
    public ResponseEntity<SellerDTO> patchSeller(@PathVariable UUID sellerId,
                                                 @RequestBody SellerPatchDTO request) {
        return ResponseEntity.ok(sellerService.patchSeller(sellerId, request));
    }

}