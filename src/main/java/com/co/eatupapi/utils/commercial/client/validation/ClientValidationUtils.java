package com.co.eatupapi.utils.commercial.client.validation;

import com.co.eatupapi.utils.commercial.client.exceptions.ClientValidationException;

import java.util.regex.Pattern;

public final class ClientValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private ClientValidationUtils() {
    }

    public static void requireObject(Object value, String message) {
        if (value == null) {
            throw new ClientValidationException(message);
        }
    }

    public static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ClientValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    public static void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ClientValidationException("Invalid email format: " + email);
        }
    }

    public static void validatePhone(String phone) {
        validateNumericValue(phone, "Phone number must contain only digits");
        if (phone.length() < 10 || phone.length() > 15) {
            throw new ClientValidationException("Phone number must contain between 10 and 15 digits");
        }
    }

    public static void validateNumericValue(String value, String message) {
        if (!DIGITS_PATTERN.matcher(value).matches()) {
            throw new ClientValidationException(message);
        }
    }
}