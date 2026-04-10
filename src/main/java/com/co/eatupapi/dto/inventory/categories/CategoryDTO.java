package com.co.eatupapi.dto.inventory.categories;

import com.co.eatupapi.domain.inventory.categories.CategoryStatus;
import java.time.LocalDate;

public class CategoryDTO {

    private String id;
    private Long cns;
    private String type;
    private String name;
    private LocalDate entryDate;
    private CategoryStatus status;

    public CategoryDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCns() {
        return cns;
    }

    public void setCns(Long cns) {
        this.cns = cns;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }
}
