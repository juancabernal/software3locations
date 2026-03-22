package com.co.eatupapi.dto.payment.invoice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear una factura")
public class InvoiceRequest {

    @Schema(description = "Número de factura", example = "INV-001")
    @NotNull(message = "Invoice number is required")
    private String invoiceNumber;

    @Schema(description = "ID de la mesa")
    @NotNull(message = "Table ID is required")
    private UUID tableId;

    @Schema(description = "ID del cliente")
    private UUID customerId;


    @Schema(description = "Porcentaje de descuento", example = "10.0")
    @PositiveOrZero(message = "Discount must be zero or positive")
    private BigDecimal discountPercentage;

    @Schema(description = "Descripción del descuento", example = "Descuento promocional")
    private String discountDescription;
}