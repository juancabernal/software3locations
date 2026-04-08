package com.co.eatupapi.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class DocumentTypeResponse {

    private UUID id;
    private String code;
    private String name;

    public DocumentTypeResponse(UUID id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

}
