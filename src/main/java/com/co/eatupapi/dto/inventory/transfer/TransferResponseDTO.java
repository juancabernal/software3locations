package com.co.eatupapi.dto.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.TransferStatus;

import java.time.LocalDateTime;

public record TransferResponseDTO(
        Long idTraslado,
        Long sedeOrigen,
        Long sedeDestino,
        LocalDateTime fecha,
        String responsable,
        Long producto,
        Integer stock,
        Integer cantidad,
        String observaciones,
        TransferStatus estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}