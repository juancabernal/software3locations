package com.co.eatupapi.dto.inventory.location;

import java.util.UUID;

public class LocationUpdateRequestedMessage {

    private final UUID id;
    private final LocationRequestDTO data;

    public LocationUpdateRequestedMessage(UUID id, LocationRequestDTO data) {
        this.id = id;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public LocationRequestDTO getData() {
        return data;
    }
}
