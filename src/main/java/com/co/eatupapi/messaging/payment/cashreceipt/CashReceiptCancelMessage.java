package com.co.eatupapi.messaging.payment.cashreceipt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CashReceiptCancelMessage {

    private UUID locationId;
    private UUID receiptId;
    private LocalDateTime eventDate;

    public CashReceiptCancelMessage(UUID locationId, UUID receiptId) {
        this.locationId = locationId;
        this.receiptId = receiptId;
        this.eventDate = LocalDateTime.now();
    }
}
