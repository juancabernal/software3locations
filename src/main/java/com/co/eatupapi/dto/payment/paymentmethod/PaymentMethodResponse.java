package com.co.eatupapi.dto.payment.paymentmethod;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Método de pago disponible")
public class PaymentMethodResponse {

    @Schema(description = "ID único del método de pago")
    private UUID id;

    @Schema(description = "Nombre del método de pago", example = "Efectivo")
    private String name;

    @Schema(description = "Descripción del método de pago")
    private String description;

    @Schema(description = "Indica si el método de pago está activo")
    private Boolean active;
}