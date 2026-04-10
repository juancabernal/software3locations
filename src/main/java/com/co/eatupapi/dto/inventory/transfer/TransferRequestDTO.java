package com.co.eatupapi.dto.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.TransferStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record TransferRequestDTO(
        @NotNull(message = "La sede de origen es obligatoria")
        @Positive(message = "La sede de origen debe ser mayor a cero")
        Long sedeOrigen,

        @NotNull(message = "La sede de destino es obligatoria")
        @Positive(message = "La sede de destino debe ser mayor a cero")
        Long sedeDestino,

        @NotNull(message = "La fecha es obligatoria")
        LocalDateTime fecha,

        @NotBlank(message = "El responsable es obligatorio")
        String responsable,

        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El producto debe ser mayor a cero")
        Long producto,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        Integer cantidad,

        String observaciones,
        TransferStatus estado
) {
}