package com.co.eatupapi.services.inventory.location;

import com.co.eatupapi.dto.inventory.location.LocationPatchDTO;
import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    List<LocationResponseDTO> findAll();

    LocationResponseDTO findById(UUID id);

    LocationResponseDTO create(LocationRequestDTO request);

    LocationResponseDTO update(UUID id, LocationRequestDTO request);

    LocationResponseDTO patchPartial(UUID id, LocationPatchDTO patch);
}
