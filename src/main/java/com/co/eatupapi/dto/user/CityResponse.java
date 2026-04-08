package com.co.eatupapi.dto.user;

import java.util.UUID;

public class CityResponse {

    private UUID id;
    private UUID departmentId;
    private String name;

    public CityResponse() {
    }

    public CityResponse(UUID id, UUID departmentId, String name) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
