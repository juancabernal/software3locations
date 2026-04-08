package com.co.eatupapi.dto.payment.invoice;

import com.co.eatupapi.domain.payment.invoice.InvoiceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Factura generada en el sistema")
public class InvoiceResponse {

    @Schema(description = "ID único de la factura")
    private UUID invoiceId;

    @Schema(description = "Número de factura", example = "INV-001")
    private String invoiceNumber;

    @Schema(description = "Estado de la factura", example = "PENDIENTE")
    private InvoiceStatus status;

    @Schema(description = "Fecha de la factura")
    private LocalDateTime invoiceDate;

    @Schema(description = "Número de la mesa", example = "5")
    private String tableNumber;

    @Schema(description = "Ubicación de la mesa", example = "Terraza")
    private String tableLocation;

    @Schema(description = "ID del cliente")
    private UUID customerId;

    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String customerName;


    @Schema(description = "Porcentaje de descuento", example = "10.0")
    private BigDecimal discountPercentage;

    @Schema(description = "Descripción del descuento", example = "Descuento promocional")
    private String discountDescription;

    @Schema(description = "Precio total", example = "15000.00")
    private BigDecimal totalPrice;
}