package com.co.eatupapi.dto.inventory.location;

import java.util.UUID;

public class LocationPatchRequestedMessage {

    private final UUID id;
    private final LocationPatchDTO data;

    public LocationPatchRequestedMessage(UUID id, LocationPatchDTO data) {
        this.id = id;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public LocationPatchDTO getData() {
        return data;
    }
}
