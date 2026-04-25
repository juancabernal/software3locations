package com.co.eatupapi.dto.commercial.sales;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class SaleDetailDTO {

    /** Opcional: si se envía, debe existir una receta con ese id. */
    private UUID recipeId;

    /**
     * Sin {@code recipeId}, puede usarse como nombre visible del ítem (línea genérica).
     */
    @Size(max = 255, message = "El nombre del ítem no puede superar los 255 caracteres")
    private String recipeName;

    @NotNull(message = "La cantidad es obligatoria en cada línea")
    @DecimalMin(value = "0.0000001", inclusive = true, message = "La cantidad debe ser mayor que cero en cada línea")
    private BigDecimal quantity;

    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    @Size(max = 500, message = "El comentario de la línea no puede superar los 500 caracteres")
    private String recipeComment;

    public UUID getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(UUID recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
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

    public String getRecipeComment() {
        return recipeComment;
    }

    public void setRecipeComment(String recipeComment) {
        this.recipeComment = recipeComment;
    }
}
