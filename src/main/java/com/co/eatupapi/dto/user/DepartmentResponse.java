package com.co.eatupapi.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class DepartmentResponse {

    private UUID id;
    private String name;

    public DepartmentResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

}
