package com.co.eatupapi.services.inventory.location.impl;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.dto.inventory.location.LocationPatchDTO;
import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;
import com.co.eatupapi.repositories.inventory.location.LocationRepository;
import com.co.eatupapi.services.inventory.location.LocationService;
import com.co.eatupapi.utils.inventory.location.exceptions.LocationResourceNotFoundException;
import com.co.eatupapi.utils.inventory.location.exceptions.LocationValidationException;
import com.co.eatupapi.utils.inventory.location.mapper.LocationMapperDomain;
import com.co.eatupapi.utils.inventory.location.validation.LocationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationResponseDTO> findAll() {
        return locationRepository.findAll()
                .stream()
                .map(LocationResponseDTO::fromDomain)
                .toList();
    }

    @Override
    public List<LocationResponseDTO> findAllActive() {
        return locationRepository.findByActiveTrue()
                .stream()
                .map(LocationResponseDTO::fromDomain)
                .toList();
    }

    @Override
    public LocationResponseDTO findById(UUID id) {
        UUID validatedId = LocationValidator.validateId(id);
        LocationDomain location = locationRepository.findById(validatedId)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con id: " + validatedId));
        return LocationResponseDTO.fromDomain(location);
    }

    @Override
    @Transactional
    public LocationResponseDTO create(LocationRequestDTO request) {
        LocationDomain domain = LocationMapperDomain.toDomain(request);
        LocationDomain saved = locationRepository.save(domain);
        return LocationResponseDTO.fromDomain(saved);
    }

    @Override
    @Transactional
    public LocationResponseDTO update(UUID id, LocationRequestDTO request) {
        UUID validatedId = LocationValidator.validateId(id);

        locationRepository.findById(validatedId)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con el id: " + validatedId));

        LocationDomain updated = LocationMapperDomain.toDomain(validatedId, request);
        LocationDomain saved = locationRepository.save(updated);
        return LocationResponseDTO.fromDomain(saved);
    }

    @Override
    @Transactional
    public LocationResponseDTO patchPartial(UUID id, LocationPatchDTO patch) {
        UUID validatedId = LocationValidator.validateId(id);
        if (patch == null || isPatchEmpty(patch)) {
            throw new LocationValidationException("Debe enviar al menos un campo para actualizar");
        }

        LocationDomain domain = locationRepository.findById(validatedId)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con id: " + validatedId));

        if (patch.getName() != null) {
            domain.setName(patch.getName());
        }
        if (patch.getCity() != null) {
            domain.setCity(patch.getCity());
        }
        if (patch.getAddress() != null) {
            domain.setAddress(patch.getAddress());
        }
        if (patch.getActive() != null) {
            domain.setActive(patch.getActive());
        }
        if (patch.getEmail() != null) {
            domain.setEmail(patch.getEmail());
        }
        if (patch.getPhoneNumber() != null) {
            domain.setPhoneNumber(patch.getPhoneNumber());
        }
        if (patch.getStartTime() != null) {
            domain.setStartTime(patch.getStartTime());
        }
        if (patch.getEndTime() != null) {
            domain.setEndTime(patch.getEndTime());
        }

        LocationDomain saved = locationRepository.save(domain);
        return LocationResponseDTO.fromDomain(saved);
    }

    private static boolean isPatchEmpty(LocationPatchDTO patch) {
        return patch.getName() == null
                && patch.getCity() == null
                && patch.getAddress() == null
                && patch.getActive() == null
                && patch.getEmail() == null
                && patch.getPhoneNumber() == null
                && patch.getStartTime() == null
                && patch.getEndTime() == null;
    }
}
