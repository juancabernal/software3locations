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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern PASSWORD_LETTER_PATTERN = Pattern.compile(".*[A-Za-z].*");
    private static final Pattern PASSWORD_DIGIT_PATTERN = Pattern.compile(".*\\d.*");

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CatalogService catalogService;
    private final UUID fixedTestLocationId;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           CatalogService catalogService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.catalogService = catalogService;
        this.fixedTestLocationId = UserLocationTestPolicy.fixedTestLocationUuid();
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        normalizeCreatePayload(request);
        validateCreatePayload(request);
        validateDuplicateEmail(request.getEmail());
        validateCatalogReferences(request.getDocumentTypeId(), request.getDepartmentId(), request.getCityId());

        UserDomain user = userMapper.toDomain(request);
        user.setLocationId(fixedTestLocationId);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());

        saveNewUser(user);
        return enrichResponse(userMapper.toResponse(user), user);
    }

    @Override
    public UserResponse getUserById(String userId) {
        UserDomain user = getAuthenticatedUser();
        validateRequestedUserId(userId, user);
        return enrichResponse(userMapper.toResponse(user), user);
    }

    @Override
    public List<UserSummaryResponse> getUsers(String status, Integer page, Integer size) {
        UserDomain user = getAuthenticatedUser();
        if (isExcludedByStatus(status, user.getStatus()) || isOutsideCurrentPage(page) || isEmptyPageSize(size)) {
            return List.of();
        }

        UserSummaryResponse summary = userMapper.toSummaryResponse(user);
        summary.setLocation(resolveLocationForResponse(user));
        return List.of(summary);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        normalizeUpdatePayload(request);
        validateUpdatePayload(request);

        UserDomain existing = getAuthenticatedUser();
        validateRequestedUserId(userId, existing);
        validateImmutableEmail(existing.getEmail(), request.getEmail());
        validateCatalogReferences(request.getDocumentTypeId(), request.getDepartmentId(), request.getCityId());

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setDocumentTypeId(request.getDocumentTypeId());
        existing.setDocumentNumber(request.getDocumentNumber());
        existing.setPhone(request.getPhone());
        existing.setBirthDate(request.getBirthDate());
        existing.setDepartmentId(request.getDepartmentId());
        existing.setCityId(request.getCityId());
        existing.setAddress(request.getAddress());
        existing.setLocationId(fixedTestLocationId);
        existing.setModifiedAt(LocalDateTime.now());

        userRepository.saveAndFlush(existing);
        return enrichResponse(userMapper.toResponse(existing), existing);
    }

    @Override
    @Transactional
    public UserResponse updateStatus(String userId, String status) {
        UserStatus newStatus = parseRequiredStatus(status);

        UserDomain existing = getAuthenticatedUser();
        validateRequestedUserId(userId, existing);
        existing.setStatus(newStatus);
        existing.setModifiedAt(LocalDateTime.now());

        userRepository.saveAndFlush(existing);
        return enrichResponse(userMapper.toResponse(existing), existing);
    }

    private UserDomain getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new UserBusinessException("Authenticated user context is not available");
        }

        String authenticatedEmail = normalizeRequiredEmail(
                authentication.getName(),
                "Authenticated user context is not available"
        );

        return userRepository.findByEmailIgnoreCase(authenticatedEmail)
                .filter(user -> user.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private UserStatus parseStatus(String status) {
        try {
            return UserStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new UserValidationException("Invalid user status value");
        }
    }

    private UserStatus parseRequiredStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new UserValidationException("User status is required");
        }
        return parseStatus(status);
    }

    private boolean isExcludedByStatus(String status, UserStatus currentStatus) {
        if (status == null || status.isBlank()) {
            return false;
        }

        return parseStatus(status) != currentStatus;
    }

    private boolean isOutsideCurrentPage(Integer page) {
        return page != null && page != 0;
    }

    private boolean isEmptyPageSize(Integer size) {
        return size != null && size <= 0;
    }

    private void validateCreatePayload(CreateUserRequest request) {
        if (request == null) {
            throw new UserValidationException("Request body is required");
        }

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

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateBirthDate(request.getBirthDate());
        validatePasswordStrength(request.getPassword());
    }

    private void validateUpdatePayload(UpdateUserRequest request) {
        if (request == null) {
            throw new UserValidationException("Request body is required");
        }

        validateRequiredText(request.getFirstName(), "firstName");
        validateRequiredText(request.getLastName(), "lastName");
        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredText(request.getDocumentNumber(), "documentNumber");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredObject(request.getBirthDate(), "birthDate");
        validateRequiredObject(request.getDepartmentId(), "departmentId");
        validateRequiredObject(request.getCityId(), "cityId");
        validateRequiredText(request.getAddress(), "address");

        validateOptionalEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateBirthDate(request.getBirthDate());
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
            throw new UserValidationException("Invalid email format");
        }
    }

    private void validateOptionalEmail(String email) {
        if (email == null) {
            return;
        }
        if (email.isBlank()) {
            throw new UserValidationException("Field 'email' cannot be empty");
        }
        validateEmail(email);
    }

    private void validatePhone(String phone) {
        if (!DIGITS_PATTERN.matcher(phone).matches()) {
            throw new UserValidationException("Phone number must contain only digits");
        }
        if (phone.length() != 10) {
            throw new UserValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new UserValidationException("Birth date must be a past date");
        }
    }

    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8 || password.length() > 72) {
            throw new UserValidationException("Password must contain between 8 and 72 characters");
        }
        if (!PASSWORD_LETTER_PATTERN.matcher(password).matches()
                || !PASSWORD_DIGIT_PATTERN.matcher(password).matches()) {
            throw new UserValidationException("Password must contain at least one letter and one digit");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new UserBusinessException("A user with that email already exists");
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (requestedEmail == null) {
            return;
        }

        String normalizedCurrentEmail = normalizeRequiredEmail(currentEmail, "Current email is invalid");
        String normalizedRequestedEmail = normalizeRequiredEmail(requestedEmail, "Requested email is invalid");

        if (!normalizedCurrentEmail.equals(normalizedRequestedEmail)) {
            throw new UserBusinessException("Email address cannot be modified once the user has been created");
        }
    }

    private void validateCatalogReferences(UUID documentTypeId, UUID departmentId, UUID cityId) {
        if (!catalogService.documentTypeExists(documentTypeId)) {
            throw new UserValidationException("Selected document type does not exist");
        }
        if (!catalogService.departmentExists(departmentId)) {
            throw new UserValidationException("Selected department does not exist");
        }
        if (!catalogService.cityExists(cityId)) {
            throw new UserValidationException("Selected city does not exist");
        }
        if (!catalogService.cityBelongsToDepartment(cityId, departmentId)) {
            throw new UserValidationException("Selected city does not belong to the selected department");
        }
    }

    private void validateRequestedUserId(String userId, UserDomain authenticatedUser) {
        if (userId == null || userId.isBlank()) {
            throw new UserValidationException("User id is required");
        }

        UUID requestedUserId;
        try {
            requestedUserId = UUID.fromString(userId.trim());
        } catch (IllegalArgumentException ex) {
            throw new UserValidationException("Invalid user id");
        }

        if (authenticatedUser.getId() == null || !authenticatedUser.getId().equals(requestedUserId)) {
            throw new UserNotFoundException("User not found");
        }
    }

    private UserResponse enrichResponse(UserResponse response, UserDomain user) {
        response.setDocumentType(catalogService.getDocumentTypeName(user.getDocumentTypeId()));
        response.setDepartment(catalogService.getDepartmentName(user.getDepartmentId()));
        response.setCity(catalogService.getCityName(user.getCityId()));
        response.setLocation(resolveLocationForResponse(user));
        return response;
    }

    private String resolveLocationForResponse(UserDomain user) {
        if (user.getLocationId() == null) {
            return UserLocationTestPolicy.FIXED_TEST_LOCATION_ID;
        }
        return user.getLocationId().toString();
    }

    private void saveNewUser(UserDomain user) {
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserBusinessException("A user with that email already exists");
        }
    }

    private void normalizeCreatePayload(CreateUserRequest request) {
        normalizeCommonPayload(
                request,
                request::getFirstName,
                request::setFirstName,
                request::getLastName,
                request::setLastName,
                request::getDocumentNumber,
                request::setDocumentNumber,
                request::getPhone,
                request::setPhone,
                request::getEmail,
                request::setEmail,
                request::getAddress,
                request::setAddress
        );
    }

    private void normalizeUpdatePayload(UpdateUserRequest request) {
        normalizeCommonPayload(
                request,
                request::getFirstName,
                request::setFirstName,
                request::getLastName,
                request::setLastName,
                request::getDocumentNumber,
                request::setDocumentNumber,
                request::getPhone,
                request::setPhone,
                request::getEmail,
                request::setEmail,
                request::getAddress,
                request::setAddress
        );
    }

    private void normalizeCommonPayload(
            Object request,
            ValueSupplier firstNameGetter,
            Consumer<String> firstNameSetter,
            ValueSupplier lastNameGetter,
            Consumer<String> lastNameSetter,
            ValueSupplier documentNumberGetter,
            Consumer<String> documentNumberSetter,
            ValueSupplier phoneGetter,
            Consumer<String> phoneSetter,
            ValueSupplier emailGetter,
            Consumer<String> emailSetter,
            ValueSupplier addressGetter,
            Consumer<String> addressSetter) {

        if (request == null) {
            return;
        }

        firstNameSetter.accept(normalizeText(firstNameGetter.get()));
        lastNameSetter.accept(normalizeText(lastNameGetter.get()));
        documentNumberSetter.accept(normalizeText(documentNumberGetter.get()));
        phoneSetter.accept(normalizeText(phoneGetter.get()));
        emailSetter.accept(normalizeOptionalEmail(emailGetter.get()));
        addressSetter.accept(normalizeText(addressGetter.get()));
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeOptionalEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeRequiredEmail(String email, String message) {
        if (email == null || email.isBlank()) {
            throw new UserValidationException(message);
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    @FunctionalInterface
    private interface ValueSupplier {
        String get();
    }
}
