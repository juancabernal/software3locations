package com.co.eatupapi.utils.inventory.location.mapper;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.repositories.inventory.location.LocationEntity;

public class LocationMapperEntity {

    private LocationMapperEntity() {}

    public static LocationEntity toEntity(LocationDomain domain) {
        LocationEntity entity = new LocationEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setCity(domain.getCity());
        entity.setAddress(domain.getAddress());
        entity.setActive(domain.isActive());
        entity.setEmail(domain.getEmail());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        return entity;
    }

    public static LocationDomain toDomain(LocationEntity entity) {
        return new LocationDomain(
                entity.getId(),
                entity.getName(),
                entity.getCity(),
                entity.getAddress(),
                entity.isActive(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }
}
