package com.co.eatupapi.controllers.payment.paymentmethod;

import com.co.eatupapi.dto.payment.paymentmethod.PaymentMethodResponse;
import com.co.eatupapi.services.payment.paymentmethod.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-methods")
@Tag(name = "Métodos de Pago", description = "Consulta de métodos de pago disponibles")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Operation(
            summary = "Listar métodos de pago activos",
            description = "Retorna los métodos de pago disponibles para aplicar en un recibo de caja."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getActivePaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.getActivePaymentMethods());
    }

    @Operation(
            summary = "Listar todos los métodos de pago",
            description = "Retorna todos los métodos de pago incluyendo los inactivos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/all")
    public ResponseEntity<List<PaymentMethodResponse>> getAllPaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods());
    }
}