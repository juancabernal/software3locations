package com.co.eatupapi.domain.payment.invoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status;

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    @Column(nullable = false)
    private UUID salesId;

    @Column(nullable = false)
    private UUID customerDiscountId;

    @Column(nullable = false)
    private UUID locationId;

    @Column
    private UUID discountId;

    @Column
    private String tableId;

    @Column(length = 150)
    private String locationName;

    @Column
    private UUID customerId;

    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(length = 255)
    private String discountDescription;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalPrice;
}
