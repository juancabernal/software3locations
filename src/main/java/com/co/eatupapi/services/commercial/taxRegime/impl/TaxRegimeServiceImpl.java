package com.co.eatupapi.services.commercial.taxRegime.impl;

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
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @PostConstruct
    public void initData() {
        LocalDateTime now = LocalDateTime.now();

        TaxRegimeDomain common = new TaxRegimeDomain();
        common.setId("1");
        common.setName("Común");
        common.setStatus(TaxRegimeStatus.ACTIVE);
        common.setCreatedDate(now);
        common.setModifiedDate(now);

        TaxRegimeDomain simplified = new TaxRegimeDomain();
        simplified.setId("2");
        simplified.setName("Simplificado");
        simplified.setStatus(TaxRegimeStatus.ACTIVE);
        simplified.setCreatedDate(now);
        simplified.setModifiedDate(now);

        TaxRegimeDomain largeTaxpayer = new TaxRegimeDomain();
        largeTaxpayer.setId("3");
        largeTaxpayer.setName("Gran Contribuyente");
        largeTaxpayer.setStatus(TaxRegimeStatus.ACTIVE);
        largeTaxpayer.setCreatedDate(now);
        largeTaxpayer.setModifiedDate(now);

        taxRegimeRepository.initializeData(List.of(common, simplified, largeTaxpayer));
    }

    @Override
    public TaxRegimeDTO createTaxRegime(TaxRegimeDTO request) {
        validateRequestBody(request);
        validateName(request.getName());
        validateIdNotEditable(request.getId());
        validateNameDoesNotExist(request.getName());

        TaxRegimeDomain taxRegime = taxRegimeMapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();
        taxRegime.setId(String.valueOf(taxRegimeRepository.findAll().size() + 1));
        taxRegime.setStatus(TaxRegimeStatus.ACTIVE);
        taxRegime.setCreatedDate(now);
        taxRegime.setModifiedDate(now);

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

        List<TaxRegimeDTO> result = new ArrayList<>();
        for (TaxRegimeDomain taxRegime : taxRegimes) {
            result.add(taxRegimeMapper.toDTO(taxRegime));
        }
        return result;
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
        existing.setModifiedDate(LocalDateTime.now());

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(existing));
    }

    @Override
    public TaxRegimeDTO changeStatus(String taxRegimeId, String status) {
        TaxRegimeStatus newStatus = parseRequiredStatus(status);

        TaxRegimeDomain existing = findTaxRegimeById(taxRegimeId);
        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(existing));
    }

    private TaxRegimeDomain findTaxRegimeById(String taxRegimeId) {
        return taxRegimeRepository.findById(taxRegimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tax regime not found with id: " + taxRegimeId));
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
        if (taxRegimeRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BusinessException("Tax regime already exists with name: " + normalizedName);
        }
    }

    private void validateNameDoesNotExistForDifferentId(String name, String id) {
        String normalizedName = name.trim();
        if (taxRegimeRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new BusinessException("Tax regime already exists with name: " + normalizedName);
        }
    }
}
