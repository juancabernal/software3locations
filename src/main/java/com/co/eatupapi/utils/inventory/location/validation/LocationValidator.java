package com.co.eatupapi.utils.inventory.location.validation;

import com.co.eatupapi.utils.inventory.location.exceptions.LocationValidationException;

import java.time.LocalTime;
import java.util.UUID;

public class LocationValidator {

    private LocationValidator() {}

    public static UUID validateId(UUID id) {
        if (id == null) {
            throw new LocationValidationException("El id de la sede es obligatorio");
        }
        return id;
    }

    public static String validateName(String name) {
        return requireNonBlank(name, "name");
    }

    public static String validateCity(String city) {
        return requireNonBlank(city, "city");
    }

    public static String validateAddress(String address) {
        return requireNonBlank(address, "address");
    }

    public static String validateEmail(String email) {
        requireNonBlank(email, "email");
        if (!email.contains("@")) {
            throw new LocationValidationException("email is not valid: " + email);
        }
        return email;
    }

    public static String validatePhoneNumber(String phoneNumber) {
        return requireNonBlank(phoneNumber, "phoneNumber");
    }

    public static LocalTime validateStartTime(LocalTime startTime) {
        return validateTime(startTime, "startTime");
    }

    public static LocalTime validateEndTime(LocalTime endTime) {
        return validateTime(endTime, "endTime");
    }

    private static LocalTime validateTime(LocalTime value, String fieldName) {
        if (value == null) {
            throw new LocationValidationException(fieldName + " must not be null");
        }
        return value;
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new LocationValidationException(fieldName + " must not be null or blank");
        }
        return value;
    }
}
