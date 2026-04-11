package com.co.eatupapi.services.commercial.purchase;

import com.co.eatupapi.dto.commercial.purchase.PurchaseDTO;
import java.util.List;


public interface PurchaseService {

    PurchaseDTO createPurchase(PurchaseDTO request);

    PurchaseDTO getPurchaseById(String purchaseId);

    List<PurchaseDTO> getPurchases(String status);

    PurchaseDTO updatePurchase(String purchaseId, PurchaseDTO request);

    PurchaseDTO updateStatus(String purchaseId, String status);

    void deletePurchase(String purchaseId);
}