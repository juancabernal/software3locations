package com.co.eatupapi.dto.commercial.sales;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public class SaleRequestDTO {

    @NotBlank(message = "El sellerId es obligatorio")
    private String sellerId;

    @NotNull(message = "La locationId es obligatoria")
    private UUID locationId;

    @NotBlank(message = "El tableId es obligatorio")
    private String tableId;

    @NotNull(message = "La lista de detalles es obligatoria")
    @Size(min = 1, message = "La venta debe tener al menos una línea de detalle")
    @Valid
    private List<SaleDetailDTO> details;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<SaleDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailDTO> details) {
        this.details = details;
    }
}
