package com.co.eatupapi.dto.payment.cashreceipt;

import com.co.eatupapi.domain.payment.cashreceipt.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos requeridos para aplicar un pago a una factura")
public class CreateCashReceiptRequest {

    @Schema(description = "ID de la factura a pagar", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "Invoice ID is required")
    private UUID invoiceId;

    @Schema(description = "Monto del pago", example = "1250.50")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Schema(description = "Medio de pago", example = "CASH", allowableValues = {"CASH", "CARD", "TRANSFER"})
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
