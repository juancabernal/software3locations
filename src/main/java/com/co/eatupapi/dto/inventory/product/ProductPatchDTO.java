package com.co.eatupapi.dto.inventory.product;

import com.co.eatupapi.domain.inventory.product.UnitOfMeasure;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ProductPatchDTO {

    private String name;
    private UUID categoryId;
    private UUID locationId;
    private UnitOfMeasure unitOfMeasure;
    private BigDecimal salePrice;
    private BigDecimal stock;
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