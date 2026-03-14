package com.co.eatupapi.services.commercial.provider;

import com.co.eatupapi.repositories.commercial.provider.ProviderRepository;
import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.domain.commercial.provider.ProviderStatus;
import com.co.eatupapi.dto.commercial.provider.ProviderDTO;
import com.co.eatupapi.utils.commercial.provider.exceptions.BusinessException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ValidationException;
import com.co.eatupapi.utils.commercial.provider.mapper.ProviderMapper;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final List<ProviderDomain> providerDomains = new ArrayList<>();

    public ProviderService(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    @PostConstruct
    public void initData() {
        providerDomains.clear();
        providerDomains.addAll(providerRepository.loadInitialProviders());
    }

    public Mono<ProviderDTO> createProvider(ProviderDTO request) {
        validateProviderPayload(request);

        ProviderDomain providerDomain = providerMapper.toDomain(request);
        providerDomain.setId(UUID.randomUUID().toString());
        providerDomain.setStatus(ProviderStatus.ACTIVE);
        providerDomain.setCreatedDate(LocalDateTime.now());
        providerDomain.setModifiedDate(LocalDateTime.now());

        providerDomains.add(providerDomain);
        return Mono.just(providerMapper.toDto(providerDomain));
    }

    public Mono<ProviderDTO> getProviderById(String providerId) {
        return findProviderById(providerId)
                .map(providerMapper::toDto);
    }

    public Flux<ProviderDTO> getProviders(String status) {
        ProviderStatus parsedStatus = parseStatus(status);

        Flux<ProviderDomain> source = Flux.fromIterable(providerDomains);
        if (parsedStatus != null) {
            source = source.filter(providerDomain -> providerDomain.getStatus() == parsedStatus);
        }

        return source.map(providerMapper::toDto);
    }

    public Mono<ProviderDTO> updateProvider(String providerId, ProviderDTO request) {
        validateProviderPayload(request);

        return findProviderById(providerId)
                .map(existing -> {
                    validateImmutableEmail(existing.getEmail(), request.getEmail());

                    existing.setBusinessName(request.getBusinessName());
                    existing.setDocumentTypeId(request.getDocumentTypeId());
                    existing.setDocumentNumber(request.getDocumentNumber());
                    existing.setTaxRegimeId(request.getTaxRegimeId());
                    existing.setResponsibleFirstName(request.getResponsibleFirstName());
                    existing.setResponsibleLastName(request.getResponsibleLastName());
                    existing.setPhone(request.getPhone());
                    existing.setDepartmentId(request.getDepartmentId());
                    existing.setCityId(request.getCityId());
                    existing.setAddress(request.getAddress());
                    existing.setBranchId(request.getBranchId());
                    existing.setModifiedDate(LocalDateTime.now());

                    return providerMapper.toDto(existing);
                });
    }

    public Mono<ProviderDTO> updateStatus(String providerId, String status) {
        ProviderStatus newStatus = parseRequiredStatus(status);

        return findProviderById(providerId)
                .map(existing -> {
                    existing.setStatus(newStatus);
                    existing.setModifiedDate(LocalDateTime.now());
                    return providerMapper.toDto(existing);
                });
    }

    private Mono<ProviderDomain> findProviderById(String providerId) {
        return Flux.fromIterable(providerDomains)
                .filter(providerDomain -> providerDomain.getId().equals(providerId))
                .next()
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Provider not found with id: " + providerId)));
    }

    private ProviderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return ProviderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid provider status value");
        }
    }

    private ProviderStatus parseRequiredStatus(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException("Invalid provider status value");
        }
        return parsedStatus;
    }

    private void validateProviderPayload(ProviderDTO request) {
        validateRequiredText(request.getBusinessName(), "businessName");
        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredText(request.getDocumentNumber(), "documentNumber");
        validateRequiredObject(request.getTaxRegimeId(), "taxRegimeId");
        validateRequiredText(request.getResponsibleFirstName(), "responsibleFirstName");
        validateRequiredText(request.getResponsibleLastName(), "responsibleLastName");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredObject(request.getDepartmentId(), "departmentId");
        validateRequiredObject(request.getCityId(), "cityId");
        validateRequiredText(request.getAddress(), "address");
        validateRequiredObject(request.getBranchId(), "branchId");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateDocumentNumber(request.getDocumentNumber());
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

    private void validatePhone(String phone) {
        if (!DIGITS_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Phone number must contain only digits");
        }
        if (phone.length() != 10) {
            throw new ValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        if (!DIGITS_PATTERN.matcher(documentNumber).matches()) {
            throw new ValidationException("Document number must contain only numeric characters");
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new BusinessException("Email address cannot be modified once the provider has been created");
        }
    }
}

