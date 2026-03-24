package com.co.eatupapi.services.commercial.provider.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.domain.commercial.provider.ProviderStatus;
import com.co.eatupapi.dto.commercial.provider.ProviderDTO;
import com.co.eatupapi.repositories.commercial.provider.ProviderRepository;
import com.co.eatupapi.services.commercial.provider.ProviderService;
import com.co.eatupapi.utils.commercial.provider.exceptions.BusinessException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ValidationException;
import com.co.eatupapi.utils.commercial.provider.mapper.ProviderMapper;
import com.co.eatupapi.utils.commercial.provider.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final String INVALID_PROVIDER_STATUS_MESSAGE = "Invalid provider status value";

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(
            ProviderRepository providerRepository,
            ProviderMapper providerMapper
    ) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    @Override
    public ProviderDTO createProvider(ProviderDTO request) {
        validateProviderPayload(request);
        validateEmailNotExists(request.getEmail());

        ProviderDomain provider = providerMapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();
        provider.setStatus(ProviderStatus.ACTIVE);
        provider.setCreatedAt(now);
        provider.setModifiedAt(now);

        return providerMapper.toDto(providerRepository.save(provider));
    }

    @Override
    public ProviderDTO getProviderById(String providerId) {
        return providerMapper.toDto(findProviderById(providerId));
    }

    @Override
    public List<ProviderDTO> getProviders(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        List<ProviderDomain> providers = parsedStatus == null
                ? providerRepository.findAll()
                : providerRepository.findByStatus(parsedStatus);

        return providers.stream()
                .sorted(Comparator.comparing(ProviderDomain::getCreatedAt))
                .map(providerMapper::toDto)
                .toList();
    }

    @Override
    public ProviderDTO updateProvider(String providerId, ProviderDTO request) {
        validateProviderPayload(request);

        ProviderDomain existing = findProviderById(providerId);
        validateImmutableEmail(existing.getEmail(), request.getEmail());

        providerMapper.updateDomain(existing, request);
        existing.setModifiedAt(LocalDateTime.now());

        return providerMapper.toDto(providerRepository.save(existing));
    }

    @Override
    public ProviderDTO updateStatus(String providerId, String status) {
        ProviderStatus newStatus = parseRequiredStatus(status);

        ProviderDomain existing = findProviderById(providerId);
        existing.setStatus(newStatus);
        existing.setModifiedAt(LocalDateTime.now());

        return providerMapper.toDto(providerRepository.save(existing));
    }

    private ProviderDomain findProviderById(String providerId) {
        UUID parsedId = parseUuid(providerId, "Provider not found with id: " + providerId);
        return providerRepository.findById(parsedId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));
    }

    private UUID parseUuid(String value, String notFoundMessage) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(notFoundMessage);
        }
    }

    private ProviderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return ProviderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(INVALID_PROVIDER_STATUS_MESSAGE);
        }
    }

    private ProviderStatus parseRequiredStatus(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException(INVALID_PROVIDER_STATUS_MESSAGE);
        }
        return parsedStatus;
    }

    private void validateProviderPayload(ProviderDTO request) {
        ValidationUtils.requireObject(request, "Provider request cannot be null");
        ValidationUtils.requireText(request.getBusinessName(), "businessName");
        ValidationUtils.requireObject(request.getDocumentTypeId(), "documentTypeId");
        ValidationUtils.requireText(request.getDocumentNumber(), "documentNumber");
        ValidationUtils.requireObject(request.getTaxRegimeId(), "taxRegimeId");
        ValidationUtils.requireText(request.getResponsibleFirstName(), "responsibleFirstName");
        ValidationUtils.requireText(request.getResponsibleLastName(), "responsibleLastName");
        ValidationUtils.requireText(request.getPhone(), "phone");
        ValidationUtils.requireText(request.getEmail(), "email");
        ValidationUtils.requireObject(request.getDepartmentId(), "departmentId");
        ValidationUtils.requireObject(request.getCityId(), "cityId");
        ValidationUtils.requireText(request.getAddress(), "address");
        ValidationUtils.requireObject(request.getBranchId(), "branchId");

        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validatePhone(request.getPhone());
        ValidationUtils.validateNumericValue(request.getDocumentNumber(),
                "Document number must contain only numeric characters");
    }

    private void validateEmailNotExists(String email) {
        providerRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new BusinessException("Email already exists: " + email);
                });
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new BusinessException("Email address cannot be modified once the provider has been created");
        }
    }
}
