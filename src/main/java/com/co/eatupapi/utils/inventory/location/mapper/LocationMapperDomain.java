package com.co.eatupapi.utils.inventory.location.mapper;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;
import com.co.eatupapi.utils.inventory.location.exceptions.LocationValidationException;

import java.util.UUID;

public class LocationMapperDomain {

    private LocationMapperDomain() {}

    public static LocationDomain toDomain(LocationRequestDTO dto) {
        if (dto == null) {
            throw new LocationValidationException("La solicitud no puede estar vacia");
        }
        return new LocationDomain(
                UUID.randomUUID(),
                dto.getName(),
                dto.getCity(),
                dto.getAddress(),
                dto.getActive(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    public static LocationDomain toDomain(UUID id, LocationRequestDTO dto) {
        if (dto == null) {
            throw new LocationValidationException("La solicitud no puede estar vacia");
        }
        return new LocationDomain(
                id,
                dto.getName(),
                dto.getCity(),
                dto.getAddress(),
                dto.getActive(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }
}
