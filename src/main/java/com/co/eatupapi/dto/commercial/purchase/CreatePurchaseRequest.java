package com.co.eatupapi.dto.commercial.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear una compra")
public class CreatePurchaseRequest {

    @Schema(description = "ID del proveedor", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "Provider ID is required")
    private UUID providerId;

    @Schema(description = "Lista de productos a comprar")
    @NotNull(message = "Items are required")
    @NotEmpty(message = "Purchase must contain at least one item")
    @Valid
    private List<CreatePurchaseItemRequest> items;
}