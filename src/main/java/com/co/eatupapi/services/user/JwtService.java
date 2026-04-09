package com.co.eatupapi.services.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Service
public class JwtService {

    private static final int MIN_SECRET_BYTES = 32;
    private static final String INSECURE_DEFAULT_SECRET =
            "EatUpApiDefaultSecretKeyThatShouldBeChangedInProduction2024!";
    private static final Set<String> DISALLOWED_SECRETS = Set.of(
            "changeme",
            "change-me",
            "default",
            "secret",
            "jwtsecret",
            "jwt-secret",
            INSECURE_DEFAULT_SECRET.toLowerCase(Locale.ROOT)
    );

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret:${JWT_SECRET:}}") String secret,
            @Value("${jwt.expiration-ms:${JWT_EXPIRATION_MS:3600000}}") long expirationMs) {
        String validatedSecret = validateSecret(secret);
        this.expirationMs = validateExpiration(expirationMs);
        this.signingKey = Keys.hmacShaKeyFor(validatedSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(normalizeEmail(email))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {
        return normalizeEmail(extractClaims(token).getSubject());
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public long getExpirationSeconds() {
        return expirationMs / 1000;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String validateSecret(String configuredSecret) {
        String normalizedSecret = configuredSecret == null ? "" : configuredSecret.trim();
        String normalizedLowercase = normalizedSecret.toLowerCase(Locale.ROOT);

        if (normalizedSecret.isBlank()) {
            throw new IllegalStateException("Invalid JWT configuration: property 'jwt.secret' is required.");
        }
        if (DISALLOWED_SECRETS.contains(normalizedLowercase)) {
            throw new IllegalStateException(
                    "Invalid JWT configuration: property 'jwt.secret' uses a disallowed insecure default value."
            );
        }
        if (normalizedSecret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "Invalid JWT configuration: property 'jwt.secret' must be at least 32 bytes for HMAC security."
            );
        }

        return normalizedSecret;
    }

    private long validateExpiration(long configuredExpirationMs) {
        if (configuredExpirationMs <= 0) {
            throw new IllegalStateException(
                    "Invalid JWT configuration: property 'jwt.expiration-ms' must be greater than 0."
            );
        }
        return configuredExpirationMs;
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
