package com.co.eatupapi.services.inventory.location.impl;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;
import com.co.eatupapi.repositories.inventory.location.LocationEntity;
import com.co.eatupapi.repositories.inventory.location.LocationRepository;
import com.co.eatupapi.services.inventory.location.LocationService;
import com.co.eatupapi.utils.inventory.location.exceptions.LocationResourceNotFoundException;
import com.co.eatupapi.utils.inventory.location.exceptions.LocationValidationException;
import com.co.eatupapi.utils.inventory.location.mapper.LocationMapperDomain;
import com.co.eatupapi.utils.inventory.location.mapper.LocationMapperEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .map(LocationMapperEntity::toDomain)
                .map(LocationResponseDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public LocationResponseDTO findById(String id) {
        validateId(id);
        LocationEntity entity = locationRepository.findById(id)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con id: " + id));
        return LocationResponseDTO.fromDomain(LocationMapperEntity.toDomain(entity));
    }

    @Override
    @Transactional
    public LocationResponseDTO create(LocationRequestDTO request) {
        validateRequest(request);
        LocationDomain domain = LocationMapperDomain.toDomain(request);
        LocationEntity saved = locationRepository.save(LocationMapperEntity.toEntity(domain));
        return LocationResponseDTO.fromDomain(LocationMapperEntity.toDomain(saved));
    }

    @Override
    @Transactional
    public LocationResponseDTO update(String id, LocationRequestDTO request) {
        validateId(id);
        validateRequest(request);

        LocationEntity existing = locationRepository.findById(id)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con id: " + id));

        LocationDomain updated = LocationMapperDomain.toDomain(id, request);
        LocationEntity toSave = LocationMapperEntity.toEntity(updated);

        // Mantiene cualquier campo que no venga del request (si luego agregas más columnas)
        toSave.setId(existing.getId());

        LocationEntity saved = locationRepository.save(toSave);
        return LocationResponseDTO.fromDomain(LocationMapperEntity.toDomain(saved));
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new LocationValidationException("El id de la sede es obligatorio");
        }
    }

    private void validateRequest(LocationRequestDTO request) {
        if (request == null) {
            throw new LocationValidationException("La solicitud no puede estar vacía");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new LocationValidationException("El nombre de la sede es obligatorio");
        }
        if (request.getCity() == null || request.getCity().isBlank()) {
            throw new LocationValidationException("La ciudad es obligatoria");
        }
        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new LocationValidationException("La dirección es obligatoria");
        }
        if (request.getActive() == null) {
            throw new LocationValidationException("El estado (active) es obligatorio");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new LocationValidationException("El email es obligatorio");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new LocationValidationException("El teléfono es obligatorio");
        }
        if (request.getStartTime() == null || request.getStartTime().isBlank()) {
            throw new LocationValidationException("La hora de apertura (startTime) es obligatoria");
        }
        if (request.getEndTime() == null || request.getEndTime().isBlank()) {
            throw new LocationValidationException("La hora de cierre (endTime) es obligatoria");
        }
    }
}
