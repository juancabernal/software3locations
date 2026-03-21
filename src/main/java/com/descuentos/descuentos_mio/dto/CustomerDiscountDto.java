package com.descuentos.descuentos_mio.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CustomerDiscountDto {

    private UUID id;
    private UUID locationId;
    private UUID customerId;
    private UUID discountId;
    private LocalDate assignedAt;

    public CustomerDiscountDto() {
    }

    public CustomerDiscountDto(UUID id, UUID locationId, UUID customerId, UUID discountId, LocalDate assignedAt) {
        this.id = id;
        this.locationId = locationId;
        this.customerId = customerId;
        this.discountId = discountId;
        this.assignedAt = assignedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getDiscountId() {
        return discountId;
    }

    public void setDiscountId(UUID discountId) {
        this.discountId = discountId;
    }

    public LocalDate getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDate assignedAt) {
        this.assignedAt = assignedAt;
    }
}
