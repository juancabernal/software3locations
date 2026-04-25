package com.co.eatupapi.dto.inventory.product;

import com.co.eatupapi.domain.inventory.product.UnitOfMeasure;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ProductPatchDTO {

    @Pattern(regexp = ".*\\S.*", message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;
    private UUID categoryId;
    private UUID locationId;
    private UnitOfMeasure unitOfMeasure;

    @DecimalMin(value = "0.001", inclusive = true, message = "El precio de venta debe ser mayor a cero")
    @DecimalMax(value = "99999999.999", message = "El precio de venta no puede superar 99,999,999.999")
    private BigDecimal salePrice;

    @DecimalMin(value = "0.000", inclusive = true, message = "El stock no puede ser negativo")
    @DecimalMax(value = "99999999.999", message = "El stock no puede superar 99,999,999.999")
    private BigDecimal stock;

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
