package com.co.eatupapi.controllers.payment.invoice;

import com.co.eatupapi.dto.payment.invoice.InvoiceRequest;
import com.co.eatupapi.dto.payment.invoice.InvoiceResponse;
import com.co.eatupapi.dto.payment.invoice.InvoiceStatusUpdateRequest;
import com.co.eatupapi.services.payment.invoice.InvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sites/{siteId}/invoices")
@Tag(name = "Facturas", description = "Gestión de facturas del sistema")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(
            summary = "Crear factura",
            description = "Crea una nueva factura en estado OPEN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Factura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
            @Parameter(description = "ID del sitio") @PathVariable UUID siteId,
            @Valid @RequestBody InvoiceRequest request) {

        InvoiceResponse response = invoiceService.createInvoice(siteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Listar facturas",
            description = "Obtiene las facturas de un sitio con paginación"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<InvoiceResponse>> getInvoices(
            @Parameter(description = "ID del sitio") @PathVariable UUID siteId,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad por página") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(invoiceService.getInvoicesBySite(siteId, pageable));
    }

    @Operation(
            summary = "Obtener factura por ID",
            description = "Retorna una factura específica de un sitio"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(
            @Parameter(description = "ID del sitio") @PathVariable UUID siteId,
            @Parameter(description = "ID de la factura") @PathVariable UUID id) {

        return ResponseEntity.ok(invoiceService.getInvoiceById(siteId, id));
    }

    @Operation(
            summary = "Actualizar estado de factura",
            description = "Permite cambiar el estado de una factura (OPEN, CLOSED, CANCELLED)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Transición inválida"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceResponse> updateStatus(
            @Parameter(description = "ID del sitio") @PathVariable UUID siteId,
            @Parameter(description = "ID de la factura") @PathVariable UUID id,
            @Valid @RequestBody InvoiceStatusUpdateRequest request) {

        return ResponseEntity.ok(invoiceService.updateStatus(siteId, id, request));
    }
}