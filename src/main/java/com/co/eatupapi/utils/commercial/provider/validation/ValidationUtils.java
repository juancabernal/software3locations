package com.co.eatupapi.utils.commercial.provider.validation;

import com.co.eatupapi.utils.commercial.provider.exceptions.ValidationException;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private ValidationUtils() {
    }

    public static void requireObject(Object value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
    }

    public static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    public static void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

    public static void validatePhone(String phone) {
        validateNumericValue(phone, "Phone number must contain only digits");
        if (phone.length() != 10) {
            throw new ValidationException("Phone number must contain exactly 10 digits");
        }
    }

    public static void validateNumericValue(String value, String message) {
        if (!DIGITS_PATTERN.matcher(value).matches()) {
            throw new ValidationException(message);
        }
    }

    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new ValidationException(
                    "Field '" + fieldName + "' must not exceed " + maxLength + " characters"
            );
        }
    }

    public static void validateExactLength(String value, int expectedLength, String fieldName) {
        if (value != null && value.length() != expectedLength) {
            throw new ValidationException(
                    "Field '" + fieldName + "' must contain exactly " + expectedLength + " characters"
            );
        }
    }
}