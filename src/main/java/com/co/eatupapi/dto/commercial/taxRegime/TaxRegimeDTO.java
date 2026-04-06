package com.co.eatupapi.dto.commercial.taxRegime;

import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeStatus;

public class TaxRegimeDTO {

    private String id;
    private String name;
    private TaxRegimeStatus status;

    public TaxRegimeDTO() {
        // Default constructor required for JSON serialization/deserialization
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
}
