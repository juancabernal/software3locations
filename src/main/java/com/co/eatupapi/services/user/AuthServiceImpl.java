package com.co.eatupapi.services.user;

import com.co.eatupapi.domain.user.UserDomain;
import com.co.eatupapi.domain.user.UserStatus;
import com.co.eatupapi.dto.user.LoginRequest;
import com.co.eatupapi.dto.user.LoginResponse;
import com.co.eatupapi.repositories.user.UserRepository;
import com.co.eatupapi.utils.user.exceptions.UserAuthenticationException;
import com.co.eatupapi.utils.user.exceptions.UserValidationException;
import com.co.eatupapi.utils.user.validation.UserValidationRules;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(UserValidationRules.EMAIL_REGEX);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        validateLoginPayload(request);

        String normalizedEmail = normalizeEmail(request.getEmail());
        UserDomain user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new UserAuthenticationException(INVALID_CREDENTIALS_MESSAGE));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserAuthenticationException(INVALID_CREDENTIALS_MESSAGE);
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UserAuthenticationException(INVALID_CREDENTIALS_MESSAGE);
        }

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }

    private void validateLoginPayload(LoginRequest request) {
        if (request == null) {
            throw new UserValidationException("Request body is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new UserValidationException("Field 'email' is required and cannot be empty");
        }
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (normalizedEmail.length() > UserValidationRules.EMAIL_MAX_LENGTH
                || !EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new UserValidationException("Invalid email format");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new UserValidationException("Field 'password' is required and cannot be empty");
        }
        if (request.getPassword().length() > UserValidationRules.PASSWORD_MAX_LENGTH) {
            throw new UserValidationException("Field 'password' must be at most 72 characters");
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
