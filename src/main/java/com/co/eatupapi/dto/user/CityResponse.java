package com.co.eatupapi.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CityResponse {

    private UUID id;
    private UUID departmentId;
    private String name;

    public CityResponse(UUID id, UUID departmentId, String name) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
    }

}
