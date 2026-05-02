package com.co.eatupapi.messaging.commercial.purchase;

import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseEvent {

    private UUID purchaseId;
    private String orderNumber;
    private UUID providerId;
    private UUID locationId;
    private BigDecimal total;
    private PurchaseStatus status;
    private LocalDateTime eventDate;

    public PurchaseEvent(UUID purchaseId, String orderNumber, UUID providerId,
                         UUID locationId, BigDecimal total, PurchaseStatus status) {
        this.purchaseId = purchaseId;
        this.orderNumber = orderNumber;
        this.providerId = providerId;
        this.locationId = locationId;
        this.total = total;
        this.status = status;
        this.eventDate = LocalDateTime.now();
    }
}
