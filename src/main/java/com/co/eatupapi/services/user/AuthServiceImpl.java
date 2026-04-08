package com.co.eatupapi.services.user;

import com.co.eatupapi.domain.user.UserDomain;
import com.co.eatupapi.dto.user.LoginRequest;
import com.co.eatupapi.dto.user.LoginResponse;
import com.co.eatupapi.repositories.user.UserRepository;
import com.co.eatupapi.utils.user.exceptions.UserBusinessException;
import com.co.eatupapi.utils.user.exceptions.UserValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

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

        UserDomain user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserBusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserBusinessException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }

    private void validateLoginPayload(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new UserValidationException("Field 'email' is required and cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new UserValidationException("Field 'password' is required and cannot be empty");
        }
    }
}
