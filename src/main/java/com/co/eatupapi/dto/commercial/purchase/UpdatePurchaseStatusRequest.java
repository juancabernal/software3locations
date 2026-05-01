package com.co.eatupapi.dto.commercial.purchase;

import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request para cambiar el estado de una compra")
public class UpdatePurchaseStatusRequest {

    @Schema(
            description = "Nuevo estado al que se desea transicionar la compra. " +
                    "Transiciones validas: CREATED → APPROVED | CANCELLED, APPROVED → RECEIVED | CANCELLED.",
            example = "APPROVED",
            allowableValues = {"APPROVED", "CANCELLED", "RECEIVED"}
    )
    @NotNull(message = "Status is required")
    private PurchaseStatus status;
}