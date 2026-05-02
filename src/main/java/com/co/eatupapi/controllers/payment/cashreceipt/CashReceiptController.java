package com.co.eatupapi.controllers.payment.cashreceipt;

import com.co.eatupapi.dto.payment.cashreceipt.CashReceiptResponse;
import com.co.eatupapi.dto.payment.cashreceipt.CreateCashReceiptRequest;
import com.co.eatupapi.services.payment.cashreceipt.CashReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/locations/{locationId}/cashreceipts")
@Tag(name = "Recibos de Caja", description = "Gestión de pagos aplicados a facturas pendientes")
public class CashReceiptController {

    private final CashReceiptService cashReceiptService;

    public CashReceiptController(CashReceiptService cashReceiptService) {
        this.cashReceiptService = cashReceiptService;
    }

    @Operation(
            summary = "Aplicar pago a una factura",
            description = "Crea un recibo de caja asociado a una factura pendiente. El invoiceId debe corresponder a una factura existente en el módulo de facturas."
    )

    @ApiResponse(responseCode = "201", description = "Recibo creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos en el request")

    @PostMapping
    public ResponseEntity<Map<String, String>> createCashReceipt(
            @Parameter(description = "ID del sitio") @PathVariable UUID locationId,
            @Valid @RequestBody CreateCashReceiptRequest request) {
        cashReceiptService.createCashReceipt(locationId, request);
        return ResponseEntity.accepted().body(Map.of("message", "Cash receipt create command published"));
    }

    @Operation(
            summary = "Listar recibos de caja",
            description = "Retorna los recibos de caja de un sitio con paginación."
    )

    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")

    @GetMapping
    public ResponseEntity<Page<CashReceiptResponse>> getCashReceipts(
            @Parameter(description = "ID del sitio") @PathVariable UUID locationId,
            @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de resultados por página") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cashReceiptService.getCashReceiptsBySite(locationId, pageable));
    }

    @Operation(
            summary = "Anular recibo de caja",
            description = "Cambia el estado del recibo a CANCELLED y registra la fecha de anulación."
    )

    @ApiResponse(responseCode = "200", description = "Recibo anulado exitosamente")
    @ApiResponse(responseCode = "400", description = "El recibo ya fue anulado previamente")
    @ApiResponse(responseCode = "404", description = "Recibo no encontrado")


    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Map<String, String>> cancelCashReceipt(
            @Parameter(description = "ID del sitio") @PathVariable UUID locationId,
            @Parameter(description = "ID del recibo a anular") @PathVariable UUID id) {
        cashReceiptService.cancelCashReceipt(locationId, id);
        return ResponseEntity.accepted().body(Map.of("message", "Cash receipt cancel command published"));
    }
}