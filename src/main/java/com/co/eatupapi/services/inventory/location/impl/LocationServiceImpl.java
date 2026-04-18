package com.co.eatupapi.services.inventory.location.impl;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.dto.inventory.location.LocationPatchDTO;
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
                .map(LocationMapperEntity::toDomain)
                .map(LocationResponseDTO::fromDomain)
                .toList();
    }

    @Override
    public LocationResponseDTO findById(UUID id) {
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
    public LocationResponseDTO update(UUID id, LocationRequestDTO request) {
        validateId(id);
        validateRequest(request);

        LocationEntity existing = locationRepository.findById(id)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con el id: " + id));

        LocationDomain updated = LocationMapperDomain.toDomain(id, request);
        LocationEntity toSave = LocationMapperEntity.toEntity(updated);

        toSave.setId(existing.getId());

        LocationEntity saved = locationRepository.save(toSave);
        return LocationResponseDTO.fromDomain(LocationMapperEntity.toDomain(saved));
    }

    @Override
    @Transactional
    public LocationResponseDTO patchPartial(UUID id, LocationPatchDTO patch) {
        validateId(id);
        if (patch == null || isPatchEmpty(patch)) {
            throw new LocationValidationException("Debe enviar al menos un campo para actualizar");
        }

        LocationEntity existing = locationRepository.findById(id)
                .orElseThrow(() -> new LocationResourceNotFoundException("Sede no encontrada con id: " + id));

        LocationDomain domain = LocationMapperEntity.toDomain(existing);

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

        LocationEntity saved = locationRepository.save(LocationMapperEntity.toEntity(domain));
        return LocationResponseDTO.fromDomain(LocationMapperEntity.toDomain(saved));
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

    private void validateId(UUID id) {
        if (id == null) {
            throw new LocationValidationException("El id de la sede es obligatorio");
        }
    }

    private void validateRequired(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new LocationValidationException(message);
        }
    }

    private void validateNotNull(Object value, String message) {
        if (value == null) {
            throw new LocationValidationException(message);
        }
    }

    private void validateRequest(LocationRequestDTO request) {
        if (request == null) {
            throw new LocationValidationException("La solicitud no puede estar vacia");
        }

        validateRequired(request.getName(), "El nombre de la sede es obligatorio");
        validateRequired(request.getCity(), "La ciudad es obligatoria");
        validateRequired(request.getAddress(), "La direccion es obligatoria");
        validateRequired(request.getEmail(), "El email es obligatorio");
        validateRequired(request.getPhoneNumber(), "El telefono es obligatorio");

        validateNotNull(request.getActive(), "El estado (active) es obligatorio");
        validateNotNull(request.getStartTime(), "La hora de apertura (startTime) es obligatoria");
        validateNotNull(request.getEndTime(), "La hora de cierre (endTime) es obligatoria");
    }
}
