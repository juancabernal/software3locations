package com.co.eatupapi.controllers.inventory.location;

import com.co.eatupapi.dto.inventory.location.LocationPatchDTO;
import com.co.eatupapi.dto.inventory.location.LocationRequestDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;
import com.co.eatupapi.services.inventory.location.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory/api/v1/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> findAll() {
        return ResponseEntity.ok(locationService.findAll());
    }


    @GetMapping("/active")
    public ResponseEntity<List<LocationResponseDTO>> findAllActive() {
        return ResponseEntity.ok(locationService.findAllActive());
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LocationResponseDTO> create(@Valid @RequestBody LocationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> update(@PathVariable UUID id,
                                                      @Valid @RequestBody LocationRequestDTO request) {
        return ResponseEntity.ok(locationService.update(id, request));
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<LocationResponseDTO> patch(@PathVariable UUID id,
                                                     @Valid @RequestBody LocationPatchDTO patch) {
        return ResponseEntity.ok(locationService.patchPartial(id, patch));
    }
}
