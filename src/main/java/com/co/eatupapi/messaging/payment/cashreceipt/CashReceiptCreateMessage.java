package com.co.eatupapi.messaging.payment.cashreceipt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CashReceiptCreateMessage {

    private UUID locationId;
    private UUID invoiceId;
    private BigDecimal amount;
    private UUID paymentMethodId;
    private LocalDateTime eventDate;

    public CashReceiptCreateMessage(UUID locationId, UUID invoiceId, BigDecimal amount, UUID paymentMethodId) {
        this.locationId = locationId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethodId = paymentMethodId;
        this.eventDate = LocalDateTime.now();
    }
}
