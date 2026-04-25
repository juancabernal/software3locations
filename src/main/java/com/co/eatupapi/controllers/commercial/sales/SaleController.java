package com.co.eatupapi.controllers.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.SalePatchDTO;
import com.co.eatupapi.dto.commercial.sales.SaleRequestDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.services.commercial.sales.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/commercial/api/v1/sales")
@Tag(name = "Ventas", description = "Gestión de ventas y líneas de pedido con recetas")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @Operation(summary = "Crear venta", description = "Registra una venta. Vendedor, sede y mesa son opcionales; si se envían, deben existir. "
            + "Cada línea puede llevar receta (opcional); sin receta hace falta precio unitario. Comentario opcional por línea.")
    @ApiResponse(responseCode = "201", description = "Venta creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos en el request")
    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(request));
    }

    @Operation(summary = "Obtener venta por id")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findById(@Parameter(description = "ID de la venta") @PathVariable UUID id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @Operation(summary = "Listar ventas")
    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> findAll() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @Operation(summary = "Actualizar venta (reemplazo completo)")
    @ApiResponse(responseCode = "200", description = "Venta actualizada")
    @ApiResponse(responseCode = "400", description = "Regla de negocio o validación")
    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> update(
            @Parameter(description = "ID de la venta") @PathVariable UUID id,
            @Valid @RequestBody SaleRequestDTO request) {
        return ResponseEntity.ok(saleService.updateSale(id, request));
    }

    @Operation(summary = "Actualizar venta parcialmente")
    @PatchMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> patch(
            @Parameter(description = "ID de la venta") @PathVariable UUID id,
            @Valid @RequestBody SalePatchDTO request) {
        return ResponseEntity.ok(saleService.patchSale(id, request));
    }

    @Operation(summary = "Eliminar venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la venta") @PathVariable UUID id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
