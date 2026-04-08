package com.co.eatupapi.services.user;

import com.co.eatupapi.domain.user.UserDomain;
import com.co.eatupapi.domain.user.UserStatus;
import com.co.eatupapi.dto.user.CreateUserRequest;
import com.co.eatupapi.dto.user.UpdateUserRequest;
import com.co.eatupapi.dto.user.UserResponse;
import com.co.eatupapi.dto.user.UserSummaryResponse;
import com.co.eatupapi.repositories.user.UserRepository;
import com.co.eatupapi.utils.user.exceptions.UserBusinessException;
import com.co.eatupapi.utils.user.exceptions.UserNotFoundException;
import com.co.eatupapi.utils.user.exceptions.UserValidationException;
import com.co.eatupapi.utils.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CatalogService catalogService;
    private final BranchClient branchClient;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           CatalogService catalogService,
                           BranchClient branchClient) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.catalogService = catalogService;
        this.branchClient = branchClient;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        validateCreatePayload(request);
        validateDuplicateEmail(request.getEmail());
        validateCatalogReferences(request.getDocumentTypeId(), request.getDepartmentId(), request.getCityId());
        branchClient.validateBranchExists(request.getBranchId());

        UserDomain user = userMapper.toDomain(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());

        userRepository.save(user);
        return enrichResponse(userMapper.toResponse(user), user);
    }

    @Override
    public UserResponse getUserById(String userId) {
        UUID id = parseUUID(userId);
        UserDomain user = findUserById(id);
        return enrichResponse(userMapper.toResponse(user), user);
    }

    @Override
    public List<UserSummaryResponse> getUsers(String status) {
        List<UserDomain> result;
        if (status == null || status.isBlank()) {
            result = userRepository.findAll();
        } else {
            UserStatus parsedStatus = parseStatus(status);
            result = userRepository.findByStatus(parsedStatus);
        }
        return result.stream().map(u -> {
            UserSummaryResponse summary = userMapper.toSummaryResponse(u);
            summary.setBranch(branchClient.getBranchName(u.getBranchId()));
            return summary;
        }).toList();
    }

    @Override
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        UUID id = parseUUID(userId);
        validateUpdatePayload(request);

        UserDomain existing = findUserById(id);
        validateImmutableEmail(existing.getEmail(), request.getEmail());
        validateCatalogReferences(request.getDocumentTypeId(), request.getDepartmentId(), request.getCityId());
        branchClient.validateBranchExists(request.getBranchId());

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setDocumentTypeId(request.getDocumentTypeId());
        existing.setDocumentNumber(request.getDocumentNumber());
        existing.setPhone(request.getPhone());
        existing.setBirthDate(request.getBirthDate());
        existing.setDepartmentId(request.getDepartmentId());
        existing.setCityId(request.getCityId());
        existing.setAddress(request.getAddress());
        existing.setBranchId(request.getBranchId());
        existing.setModifiedAt(LocalDateTime.now());

        userRepository.save(existing);
        return enrichResponse(userMapper.toResponse(existing), existing);
    }

    @Override
    public UserResponse updateStatus(String userId, String status) {
        UUID id = parseUUID(userId);
        UserStatus newStatus = parseRequiredStatus(status);

        UserDomain existing = findUserById(id);
        existing.setStatus(newStatus);
        existing.setModifiedAt(LocalDateTime.now());

        userRepository.save(existing);
        return enrichResponse(userMapper.toResponse(existing), existing);
    }

    // ── Internal helpers ──────────────────────────────────────────

    private UserDomain findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private UUID parseUUID(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new UserValidationException("Invalid UUID format: " + value);
        }
    }

    private UserStatus parseStatus(String status) {
        try {
            return UserStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new UserValidationException("Invalid user status value: " + status);
        }
    }

    private UserStatus parseRequiredStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new UserValidationException("User status is required");
        }
        return parseStatus(status);
    }

    // ── Validation helpers ────────────────────────────────────────

    private void validateCreatePayload(CreateUserRequest request) {
        validateRequiredText(request.getFirstName(), "firstName");
        validateRequiredText(request.getLastName(), "lastName");
        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredText(request.getDocumentNumber(), "documentNumber");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredText(request.getPassword(), "password");
        validateRequiredObject(request.getBirthDate(), "birthDate");
        validateRequiredObject(request.getDepartmentId(), "departmentId");
        validateRequiredObject(request.getCityId(), "cityId");
        validateRequiredText(request.getAddress(), "address");
        validateRequiredObject(request.getBranchId(), "branchId");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
    }

    private void validateUpdatePayload(UpdateUserRequest request) {
        validateRequiredText(request.getFirstName(), "firstName");
        validateRequiredText(request.getLastName(), "lastName");
        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredText(request.getDocumentNumber(), "documentNumber");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredObject(request.getBirthDate(), "birthDate");
        validateRequiredObject(request.getDepartmentId(), "departmentId");
        validateRequiredObject(request.getCityId(), "cityId");
        validateRequiredText(request.getAddress(), "address");
        validateRequiredObject(request.getBranchId(), "branchId");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new UserValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new UserValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserValidationException(
                    "Invalid email format: '" + email + "'. Expected format: example@domain.com"
            );
        }
    }

    private void validatePhone(String phone) {
        if (!DIGITS_PATTERN.matcher(phone).matches()) {
            throw new UserValidationException("Phone number must contain only digits");
        }
        if (phone.length() != 10) {
            throw new UserValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserBusinessException("A user with email '" + email + "' already exists");
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (requestedEmail != null && !currentEmail.equals(requestedEmail)) {
            throw new UserBusinessException("Email address cannot be modified once the user has been created");
        }
    }

    private void validateCatalogReferences(UUID documentTypeId, UUID departmentId, UUID cityId) {
        if (!catalogService.documentTypeExists(documentTypeId)) {
            throw new UserValidationException("Document type not found with id: " + documentTypeId);
        }
        if (!catalogService.departmentExists(departmentId)) {
            throw new UserValidationException("Department not found with id: " + departmentId);
        }
        if (!catalogService.cityExists(cityId)) {
            throw new UserValidationException("City not found with id: " + cityId);
        }
    }

    // ── Response enrichment ───────────────────────────────────────

    /**
     * Enriches UserResponse with resolved names for documentType, department, city, and branch.
     */
    private UserResponse enrichResponse(UserResponse response, UserDomain user) {
        response.setDocumentType(catalogService.getDocumentTypeName(user.getDocumentTypeId()));
        response.setDepartment(catalogService.getDepartmentName(user.getDepartmentId()));
        response.setCity(catalogService.getCityName(user.getCityId()));
        response.setBranch(branchClient.getBranchName(user.getBranchId()));
        return response;
    }
}
