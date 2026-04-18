package com.co.eatupapi.utils.inventory.location.mapper;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;

import java.util.UUID;

public class LocationMapperDomain {

    private LocationMapperDomain() {}

    public static LocationDomain toDomain(LocationRequestDTO dto) {
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
