package com.co.eatupapi.dto.inventory.product;

import com.co.eatupapi.domain.inventory.product.UnitOfMeasure;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ProductRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres")
    private String name;

    @NotNull(message = "El id de la categoria es obligatorio")
    private UUID categoryId;

    @NotNull(message = "El id de la sede es obligatorio")
    private UUID locationId;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.001", inclusive = true, message = "El precio de venta debe ser mayor a cero")
    @DecimalMax(value = "99999999.999", message = "El precio de venta no puede superar 99,999,999.999")
    private BigDecimal salePrice;

    @NotNull(message = "El stock es obligatorio")
    @DecimalMin(value = "0.000", inclusive = true, message = "El stock no puede ser negativo")
    @DecimalMax(value = "99999999.999", message = "El stock no puede superar 99,999,999.999")
    private BigDecimal stock;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @PastOrPresent(message = "La fecha de inicio no puede ser una fecha futura")
    private LocalDate startDate;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }

    public UnitOfMeasure getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public BigDecimal getStock() { return stock; }
    public void setStock(BigDecimal stock) { this.stock = stock; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
