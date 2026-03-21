package com.co.eatupapi.services.commercial.seller;

import com.co.eatupapi.domain.commercial.seller.SellerDomain;
import com.co.eatupapi.domain.commercial.seller.SellerStatus;
import com.co.eatupapi.dto.commercial.seller.SellerDTO;
import com.co.eatupapi.repositories.commercial.seller.SellerRepository;
import com.co.eatupapi.utils.commercial.seller.exceptions.SellerBusinessException;
import com.co.eatupapi.utils.commercial.seller.exceptions.SellerNotFoundException;
import com.co.eatupapi.utils.commercial.seller.exceptions.SellerValidationException;
import com.co.eatupapi.utils.commercial.seller.mapper.SellerMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final List<SellerDomain> sellers = new ArrayList<>();

    public SellerService(SellerRepository sellerRepository, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
    }

    @PostConstruct
    public void initData() {
        sellers.clear();
        sellers.addAll(sellerRepository.loadInitialSellers());
    }

    public SellerDTO createSeller(SellerDTO request) {
        validateSellerPayload(request);
        validateDuplicateEmail(request.getEmail());
        validateDuplicateIdentification(request.getIdentificationNumber());

        SellerDomain sellerDomain = sellerMapper.toDomain(request);
        sellerDomain.setId(UUID.randomUUID().toString());
        sellerDomain.setStatus(SellerStatus.ACTIVE);
        sellerDomain.setCreatedDate(LocalDateTime.now());
        sellerDomain.setModifiedDate(LocalDateTime.now());

        sellers.add(sellerDomain);
        return sellerMapper.toDto(sellerDomain);
    }

    public SellerDTO getSellerById(String sellerId) {
        SellerDomain seller = findSellerById(sellerId);
        return sellerMapper.toDto(seller);
    }

    public List<SellerDTO> getSellers(String status) {
        SellerStatus parsedStatus = parseStatus(status);

        List<SellerDTO> result = new ArrayList<>();
        for (SellerDomain seller : sellers) {
            if (parsedStatus == null || seller.getStatus() == parsedStatus) {
                result.add(sellerMapper.toDto(seller));
            }
        }
        return result;
    }

    public SellerDTO updateSeller(String sellerId, SellerDTO request) {
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

        return sellerMapper.toDto(existing);
    }

    public SellerDTO updateStatus(String sellerId, String status) {
        SellerStatus newStatus = parseRequiredStatus(status);

        SellerDomain existing = findSellerById(sellerId);
        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());
        return sellerMapper.toDto(existing);
    }

    private SellerDomain findSellerById(String sellerId) {
        for (SellerDomain seller : sellers) {
            if (seller.getId().equals(sellerId)) {
                return seller;
            }
        }
        throw new SellerNotFoundException("Seller not found with id: " + sellerId);
    }

    private SellerStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return SellerStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SellerValidationException("Invalid seller status value");
        }
    }

    private SellerStatus parseRequiredStatus(String status) {
        SellerStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new SellerValidationException("Seller status is required");
        }
        return parsedStatus;
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
            throw new SellerValidationException("Invalid email format: " + email);
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
        if (commission < MIN_COMMISSION) {
            throw new SellerValidationException("Commission percentage cannot be negative");
        }
        if (commission > MAX_COMMISSION) {
            throw new SellerValidationException("Commission percentage cannot exceed 30%");
        }
    }

    private void validateDuplicateEmail(String email) {
        for (SellerDomain seller : sellers) {
            if (seller.getEmail().equals(email)) {
                throw new SellerBusinessException("A seller with email '" + email + "' already exists");
            }
        }
    }

    private void validateDuplicateIdentification(String identificationNumber) {
        for (SellerDomain seller : sellers) {
            if (seller.getIdentificationNumber().equals(identificationNumber)) {
                throw new SellerBusinessException("A seller with identification number '" + identificationNumber + "' already exists");
            }
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new SellerBusinessException("Email address cannot be modified once the seller has been created");
        }
    }
}