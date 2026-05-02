package com.co.eatupapi.dto.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class RecipePreparationTraceResponseDTO {

    private UUID id;
    private UUID saleId;
    private UUID saleDetailId;
    private UUID recipeId;
    private RecipePreparationTraceStatus status;
    private String observation;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSaleId() { return saleId; }
    public void setSaleId(UUID saleId) { this.saleId = saleId; }
    public UUID getSaleDetailId() { return saleDetailId; }
    public void setSaleDetailId(UUID saleDetailId) { this.saleDetailId = saleDetailId; }
    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(UUID recipeId) { this.recipeId = recipeId; }
    public RecipePreparationTraceStatus getStatus() { return status; }
    public void setStatus(RecipePreparationTraceStatus status) { this.status = status; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
}
