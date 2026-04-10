package com.co.eatupapi.dto.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.TransferStatus;
import jakarta.validation.constraints.NotNull;

public record TransferStatusUpdateDTO(
        @NotNull(message = "El estado es obligatorio")
        TransferStatus estado
) {}
