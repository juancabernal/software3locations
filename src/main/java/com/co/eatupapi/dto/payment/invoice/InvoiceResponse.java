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

    @Schema(description = "Estado de la factura", example = "OPEN")
    private InvoiceStatus status;

    @Schema(description = "Fecha de la factura")
    private LocalDateTime invoiceDate;

    @Schema(description = "ID de la venta asociada")
    private UUID salesId;

    @Schema(description = "ID de la relación cliente-descuento asociada")
    private UUID customerDiscountId;

    @Schema(description = "ID de la sede asociada")
    private UUID locationId;

    @Schema(description = "ID del descuento asociado")
    private UUID discountId;

    @Schema(description = "Identificador de mesa obtenido desde sales")
    private String tableId;

    @Schema(description = "Nombre de la sede asociada")
    private String locationName;

    @Schema(description = "ID del cliente")
    private UUID customerId;

    @Schema(description = "Porcentaje de descuento resuelto desde descuentos")
    private BigDecimal discountPercentage;

    @Schema(description = "Descripción del descuento resuelto desde descuentos")
    private String discountDescription;

    @Schema(description = "Precio total", example = "15000.00")
    private BigDecimal totalPrice;
}
