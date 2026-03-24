package com.co.eatupapi.domain.commercial.taxRegime;

import java.time.LocalDateTime;

public class TaxRegimeDomain {

    private String id;
    private String name;
    private TaxRegimeStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public TaxRegimeDomain() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaxRegimeStatus getStatus() {
        return status;
    }

    public void setStatus(TaxRegimeStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
