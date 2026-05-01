package com.co.eatupapi.domain.commercial.sales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "recipe_preparation_traces")
public class RecipePreparationTraceDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private SaleDomain sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_detail_id", nullable = false)
    private SaleDetailDomain saleDetail;

    @Column(name = "recipe_id", nullable = false)
    private UUID recipeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipePreparationTraceStatus status;

    @Column(length = 500)
    private String observation;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public RecipePreparationTraceDomain() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public SaleDomain getSale() { return sale; }
    public void setSale(SaleDomain sale) { this.sale = sale; }
    public SaleDetailDomain getSaleDetail() { return saleDetail; }
    public void setSaleDetail(SaleDetailDomain saleDetail) { this.saleDetail = saleDetail; }
    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(UUID recipeId) { this.recipeId = recipeId; }
    public RecipePreparationTraceStatus getStatus() { return status; }
    public void setStatus(RecipePreparationTraceStatus status) { this.status = status; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getModifiedDate() { return modifiedDate; }
}
