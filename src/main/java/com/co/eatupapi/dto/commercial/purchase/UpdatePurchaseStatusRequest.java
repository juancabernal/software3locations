package com.co.eatupapi.dto.commercial.purchase;

import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePurchaseStatusRequest {

    @NotNull(message = "Status is required")
    private PurchaseStatus status;
}