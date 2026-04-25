package com.co.eatupapi.dto.commercial.sales;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public class SaleDetailDTO {

    @NotNull(message = "El recipeId es obligatorio en cada línea")
    private UUID recipeId;

    @Size(max = 255, message = "El lineDisplayName no puede superar los 255 caracteres")
    private String lineDisplayName;

    @NotNull(message = "La cantidad es obligatoria en cada línea")
    @DecimalMin(value = "0.0000001", message = "La cantidad debe ser mayor que cero")
    private BigDecimal quantity;

    @NotNull(message = "El precio unitario es obligatorio en cada línea")
    @DecimalMin(value = "0.0000001", message = "El precio unitario debe ser mayor que cero")
    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    @NotNull(message = "El recipeLineComment es obligatorio en cada línea")
    @Size(max = 500, message = "El recipeLineComment no puede superar los 500 caracteres")
    private String recipeLineComment;

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public String getLineDisplayName() {
        return lineDisplayName;
    }

    public void setLineDisplayName(String lineDisplayName) {
        this.lineDisplayName = lineDisplayName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getRecipeLineComment() {
        return recipeLineComment;
    }

    public void setRecipeLineComment(String recipeLineComment) {
        this.recipeLineComment = recipeLineComment;
    }
}
