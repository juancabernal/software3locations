package com.co.eatupapi.services.commercial.seller;

import com.co.eatupapi.domain.commercial.seller.SellerDomain;
import com.co.eatupapi.domain.commercial.seller.SellerStatus;
import com.co.eatupapi.dto.commercial.seller.SellerDTO;
import com.co.eatupapi.repositories.commercial.seller.SellerRepository;
import com.co.eatupapi.utils.commercial.seller.exceptions.SellerBusinessException;
import com.co.eatupapi.utils.commercial.seller.exceptions.SellerNotFoundException;
import com.co.eatupapi.utils.commercial.seller.exceptions.SellerValidationException;
import com.co.eatupapi.utils.commercial.seller.mapper.SellerMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class SellerService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");
    private static final double MAX_COMMISSION = 30.0;
    private static final double MIN_COMMISSION = 0.0;

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    public SellerService(SellerRepository sellerRepository, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
    }

    public SellerDTO createSeller(SellerDTO request) {
        validateSellerPayload(request);
        validateDuplicateEmail(request.getEmail());
        validateDuplicateIdentification(request.getIdentificationNumber());

        SellerDomain sellerDomain = sellerMapper.toDomain(request);
        sellerDomain.setStatus(SellerStatus.ACTIVE);
        sellerDomain.setCreatedDate(LocalDateTime.now());
        sellerDomain.setModifiedDate(LocalDateTime.now());

        sellerRepository.save(sellerDomain);
        return sellerMapper.toDto(sellerDomain);
    }

    public SellerDTO getSellerById(UUID sellerId) {
        SellerDomain seller = findSellerById(sellerId);
        return sellerMapper.toDto(seller);
    }

    public List<SellerDTO> getSellers(String status) {
        List<SellerDomain> result;
        if (status == null || status.isBlank()) {
            result = sellerRepository.findAll();
        } else {
            SellerStatus parsedStatus = parseStatus(status);
            result = sellerRepository.findByStatus(parsedStatus);
        }
        return result.stream().map(sellerMapper::toDto).toList();
    }

    public SellerDTO updateSeller(UUID sellerId, SellerDTO request) {
        validateSellerPayload(request);

        SellerDomain existing = findSellerById(sellerId);
        validateImmutableEmail(existing.getEmail(), request.getEmail());

        existing.setDocumentType(request.getDocumentType());
        existing.setLocationId(request.getLocationId());
        existing.setIdentificationNumber(request.getIdentificationNumber());
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setPhone(request.getPhone());
        existing.setCommissionPercentage(request.getCommissionPercentage());
        existing.setModifiedDate(LocalDateTime.now());

        sellerRepository.save(existing);
        return sellerMapper.toDto(existing);
    }

    public SellerDTO updateStatus(UUID sellerId, String status) {
        SellerStatus newStatus = parseRequiredStatus(status);

        SellerDomain existing = findSellerById(sellerId);
        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        sellerRepository.save(existing);
        return sellerMapper.toDto(existing);
    }

    private SellerDomain findSellerById(UUID sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller not found with id: " + sellerId));
    }

    private SellerStatus parseStatus(String status) {
        try {
            return SellerStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SellerValidationException("Invalid seller status value");
        }
    }

    private SellerStatus parseRequiredStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new SellerValidationException("Seller status is required");
        }
        return parseStatus(status);
    }

    private void validateSellerPayload(SellerDTO request) {
        validateRequiredText(request.getDocumentType(), "documentType");
        validateRequiredObject(request.getLocationId(), "locationId");
        validateRequiredText(request.getIdentificationNumber(), "identificationNumber");
        validateRequiredText(request.getFirstName(), "firstName");
        validateRequiredText(request.getLastName(), "lastName");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredObject(request.getCommissionPercentage(), "commissionPercentage");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateCommissionPercentage(request.getCommissionPercentage());
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new SellerValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new SellerValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new SellerValidationException(
                    "Invalid email format: '" + email + "'. Expected format: example@domain.com"
            );
        }
    }

    private void validatePhone(String phone) {
        if (!DIGITS_PATTERN.matcher(phone).matches()) {
            throw new SellerValidationException("Phone number must contain only digits");
        }
        if (phone.length() != 10) {
            throw new SellerValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateCommissionPercentage(Double commission) {
        if (commission == null) {
            throw new SellerValidationException("Field 'commissionPercentage' is required and cannot be empty");
        }
        if (commission < MIN_COMMISSION) {
            throw new SellerValidationException("Commission percentage cannot be negative");
        }
        if (commission > MAX_COMMISSION) {
            throw new SellerValidationException("Commission percentage cannot exceed 30%");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (sellerRepository.existsByEmail(email)) {
            throw new SellerBusinessException("A seller with email '" + email + "' already exists");
        }
    }

    private void validateDuplicateIdentification(String identificationNumber) {
        if (sellerRepository.existsByIdentificationNumber(identificationNumber)) {
            throw new SellerBusinessException("A seller with identification number '" + identificationNumber + "' already exists");
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new SellerBusinessException("Email address cannot be modified once the seller has been created");
        }
    }
}