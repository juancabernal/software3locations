package com.co.eatupapi.services.commercial.taxRegime.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Objects;



import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeDomain;
import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeStatus;
import com.co.eatupapi.dto.commercial.taxRegime.TaxRegimeDTO;
import com.co.eatupapi.repositories.commercial.taxRegime.TaxRegimeRepository;
import com.co.eatupapi.services.commercial.taxRegime.TaxRegimeService;
import com.co.eatupapi.utils.commercial.provider.exceptions.BusinessException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ValidationException;
import com.co.eatupapi.utils.commercial.provider.validation.ValidationUtils;
import com.co.eatupapi.utils.commercial.taxRegime.mapper.TaxRegimeMapper;
import org.springframework.stereotype.Service;

@Service
public class TaxRegimeServiceImpl implements TaxRegimeService {

    private static final String INVALID_TAX_REGIME_STATUS_MESSAGE = "Tax regime status must be ACTIVE or INACTIVE";

    private final TaxRegimeRepository taxRegimeRepository;
    private final TaxRegimeMapper taxRegimeMapper;

    public TaxRegimeServiceImpl(TaxRegimeRepository taxRegimeRepository, TaxRegimeMapper taxRegimeMapper) {
        this.taxRegimeRepository = taxRegimeRepository;
        this.taxRegimeMapper = taxRegimeMapper;
    }

    @Override
    public TaxRegimeDTO createTaxRegime(TaxRegimeDTO request) {
        validateRequestBody(request);
        validateName(request.getName());
        validateIdNotEditable(request.getId());
        validateNameDoesNotExist(request.getName());

        TaxRegimeDomain taxRegime = taxRegimeMapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();
        taxRegime.setStatus(TaxRegimeStatus.ACTIVE);
        taxRegime.setCreatedAt(now);
        taxRegime.setModifiedAt(now);

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(taxRegime));
    }

    @Override
    public TaxRegimeDTO getTaxRegimeById(String taxRegimeId) {
        return taxRegimeMapper.toDTO(findTaxRegimeById(taxRegimeId));
    }

    @Override
    public List<TaxRegimeDTO> getTaxRegimes(String status) {
        TaxRegimeStatus parsedStatus = parseStatus(status);
        List<TaxRegimeDomain> taxRegimes = parsedStatus == null
                ? taxRegimeRepository.findAll()
                : taxRegimeRepository.findByStatus(parsedStatus);

        return taxRegimes.stream()
                .sorted(Comparator.comparing(TaxRegimeDomain::getCreatedAt))
                .map(taxRegimeMapper::toDTO)
                .toList();
    }

    @Override
    public TaxRegimeDTO updateTaxRegime(String taxRegimeId, TaxRegimeDTO request) {
        validateRequestBody(request);
        validateName(request.getName());
        validateStatus(request.getStatus());
        validateIdNotEditable(request.getId());

        TaxRegimeDomain existing = findTaxRegimeById(taxRegimeId);
        validateNameDoesNotExistForDifferentId(request.getName(), existing.getId());

        taxRegimeMapper.updateDomain(existing, request);
        existing.setModifiedAt(LocalDateTime.now());

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(existing));
    }

    @Override
    public TaxRegimeDTO changeStatus(String taxRegimeId, String status) {
        TaxRegimeStatus newStatus = parseRequiredStatus(status);

        TaxRegimeDomain existing = findTaxRegimeById(taxRegimeId);
        existing.setStatus(newStatus);
        existing.setModifiedAt(LocalDateTime.now());

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(existing));
    }

    private TaxRegimeDomain findTaxRegimeById(String taxRegimeId) {
        UUID parsedId = parseUuid(taxRegimeId, "Tax regime not found with id: " + taxRegimeId);
        return taxRegimeRepository.findById(parsedId)
                .orElseThrow(() -> new ResourceNotFoundException("Tax regime not found with id: " + taxRegimeId));
    }

    private UUID parseUuid(String value, String notFoundMessage) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(notFoundMessage);
        }
    }

    private TaxRegimeStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return TaxRegimeStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(INVALID_TAX_REGIME_STATUS_MESSAGE);
        }
    }

    private TaxRegimeStatus parseRequiredStatus(String status) {
        TaxRegimeStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException(INVALID_TAX_REGIME_STATUS_MESSAGE);
        }
        return parsedStatus;
    }

    private void validateRequestBody(TaxRegimeDTO request) {
        ValidationUtils.requireObject(request, "Tax regime request cannot be null");
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Tax regime name cannot be null or empty");
        }
    }

    private void validateStatus(TaxRegimeStatus status) {
        if (status == null) {
            throw new ValidationException(INVALID_TAX_REGIME_STATUS_MESSAGE);
        }
    }

    private void validateIdNotEditable(String id) {
        if (id != null && !id.isBlank()) {
            throw new BusinessException("Tax regime id cannot be modified");
        }
    }

    private void validateNameDoesNotExist(String name) {
        String normalizedName = name.trim();
        boolean exists = taxRegimeRepository.findAll().stream()
                .map(TaxRegimeDomain::getName)
                .filter(Objects::nonNull)
                .anyMatch(existingName -> existingName.equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new BusinessException("Tax regime already exists with name: " + normalizedName);
        }
    }

    private void validateNameDoesNotExistForDifferentId(String name, UUID id) {
        String normalizedName = name.trim();
        boolean exists = taxRegimeRepository.findAll().stream()
                .filter(taxRegime -> !taxRegime.getId().equals(id))
                .map(TaxRegimeDomain::getName)
                .filter(Objects::nonNull)
                .anyMatch(existingName -> existingName.equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new BusinessException("Tax regime already exists with name: " + normalizedName);
        }
    }
}