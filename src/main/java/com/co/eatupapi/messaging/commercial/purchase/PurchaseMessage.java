package com.co.eatupapi.messaging.commercial.purchase;

import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseMessage {

    private UUID purchaseId;
    private String orderNumber;
    private UUID providerId;
    private UUID locationId;
    private List<PurchaseItemMessage> items;
    private BigDecimal total;
    private PurchaseStatus status;
    private LocalDateTime eventDate;
    private PurchaseAction action;

    public PurchaseMessage(UUID purchaseId, String orderNumber, UUID providerId,
                           UUID locationId, List<PurchaseItemMessage> items,
                           BigDecimal total, PurchaseStatus status, PurchaseAction action) {
        this.purchaseId = purchaseId;
        this.orderNumber = orderNumber;
        this.providerId = providerId;
        this.locationId = locationId;
        this.items = items;
        this.total = total;
        this.status = status;
        this.action = action;
        this.eventDate = LocalDateTime.now();
    }
}
