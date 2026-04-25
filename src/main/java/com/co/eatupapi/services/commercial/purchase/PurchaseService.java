package com.co.eatupapi.services.commercial.purchase;

import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import com.co.eatupapi.dto.commercial.purchase.CreatePurchaseRequest;
import com.co.eatupapi.dto.commercial.purchase.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PurchaseService {

    PurchaseResponse createPurchase(UUID locationId,CreatePurchaseRequest request);

    PurchaseResponse getPurchaseById(UUID locationId,UUID purchaseId);

    Page<PurchaseResponse> getPurchases(UUID locationId, PurchaseStatus status, Pageable pageable);

    PurchaseResponse updatePurchase(UUID locationId,UUID purchaseId, CreatePurchaseRequest request);

    PurchaseResponse updateStatus(UUID locationId,UUID purchaseId, PurchaseStatus status);

    void deletePurchase(UUID locationId,UUID purchaseId);
}