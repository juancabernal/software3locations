package com.co.eatupapi.services.inventory.location;

import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;

import java.util.List;

public interface LocationService {
    List<LocationResponseDTO> findAll();

    LocationResponseDTO findById(String id);

    LocationResponseDTO create(LocationRequestDTO request);

    LocationResponseDTO update(String id, LocationRequestDTO request);
}
