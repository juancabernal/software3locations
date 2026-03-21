package com.co.eatupapi.dto.commercial.table;

public class TableSummaryDTO {

    private Long totalRegistered;
    private Long available;
    private Long occupied;
    private Long reserved;
    private String venueId;

    public TableSummaryDTO() {
    }

    public Long getTotalRegistered() {
        return totalRegistered;
    }

    public void setTotalRegistered(Long totalRegistered) {
        this.totalRegistered = totalRegistered;
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public Long getOccupied() {
        return occupied;
    }

    public void setOccupied(Long occupied) {
        this.occupied = occupied;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }
}