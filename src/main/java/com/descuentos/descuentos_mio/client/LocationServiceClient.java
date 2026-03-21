package com.descuentos.descuentos_mio.client;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class LocationServiceClient {

    private static final UUID LOCATION_ID_1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID LOCATION_ID_2 = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID LOCATION_ID_3 = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    private final List<UUID> locationIds = List.of(LOCATION_ID_1, LOCATION_ID_2, LOCATION_ID_3);

    public Optional<UUID> findLocationById(UUID locationId) {
        return locationIds.stream()
                .filter(id -> id.equals(locationId))
                .findFirst();
    }

    public List<UUID> getAvailableLocationIds() {
        return locationIds;
    }
}
