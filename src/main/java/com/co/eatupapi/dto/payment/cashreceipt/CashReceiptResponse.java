package com.co.eatupapi.dto.payment.cashreceipt;

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
@Schema(description = "Recibo de caja generado tras aplicar un pago")
public class CashReceiptResponse {

    @Schema(description = "ID único del recibo")
    private UUID id;

    @Schema(description = "ID de la factura asociada")
    private UUID invoiceId;

    @Schema(description = "Monto pagado", example = "1250.50")
    private BigDecimal amount;

    @Schema(description = "Medio de pago utilizado", example = "CASH")
    private String paymentMethod;

    @Schema(description = "Estado del recibo", example = "PAID")
    private String status;

    @Schema(description = "Fecha y hora de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de anulación, null si está activo")
    private LocalDateTime cancelledAt;
}