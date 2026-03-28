package com.co.eatupapi.services.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Locale;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String INSECURE_DEFAULT_SECRET =
            "EatUpApiDefaultSecretKeyThatShouldBeChangedInProduction2024!";

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret:}") String secret,
            @Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        this.signingKey = resolveSigningKey(secret);
        this.expirationMs = expirationMs > 0 ? expirationMs : 3600000L;
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
        } catch (Exception ex) {
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

    private SecretKey resolveSigningKey(String configuredSecret) {
        String normalizedSecret = configuredSecret == null ? "" : configuredSecret.trim();

        if (normalizedSecret.isBlank()
                || INSECURE_DEFAULT_SECRET.equals(normalizedSecret)
                || normalizedSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            LOGGER.warn("JWT secret is missing or insecure; using an ephemeral in-memory key for this process.");
            byte[] randomKey = new byte[64];
            SECURE_RANDOM.nextBytes(randomKey);
            return Keys.hmacShaKeyFor(randomKey);
        }

        return Keys.hmacShaKeyFor(normalizedSecret.getBytes(StandardCharsets.UTF_8));
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
