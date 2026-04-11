package com.co.eatupapi.domain.commercial.purchase;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase")
public class PurchaseDomain {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "order_number", nullable = false, unique = true, length = 64)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderDomain provider;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItemDomain> items = new ArrayList<>();

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseStatus status;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public PurchaseDomain() {}

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public ProviderDomain getProvider() { return provider; }
    public void setProvider(ProviderDomain provider) { this.provider = provider; }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public List<PurchaseItemDomain> getItems() { return items; }

    public void replaceItems(List<PurchaseItemDomain> newItems) {
        this.items.clear();
        if (newItems != null) {
            for (PurchaseItemDomain item : newItems) {
                item.setPurchase(this);
                this.items.add(item);
            }
        }
    }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public PurchaseStatus getStatus() { return status; }
    public void setStatus(PurchaseStatus status) { this.status = status; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
}