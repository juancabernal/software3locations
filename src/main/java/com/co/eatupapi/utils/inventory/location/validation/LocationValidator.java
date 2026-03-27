package com.co.eatupapi.utils.inventory.location.validation;

public class LocationValidator {

    private LocationValidator() {}

    private static final String TIME_REGEX = "^([01]\\d|2[0-3]):[0-5]\\d$";

    public static String validateId(String id) {
        return requireNonBlank(id, "id");
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
            throw new IllegalArgumentException("email is not valid: " + email);
        }
        return email;
    }

    public static String validatePhoneNumber(String phoneNumber) {
        return requireNonBlank(phoneNumber, "phoneNumber");
    }

    public static String validateStartTime(String startTime) {
        return validateTime(startTime, "startTime");
    }

    public static String validateEndTime(String endTime) {
        return validateTime(endTime, "endTime");
    }

    private static String validateTime(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank");
        }
        if (!value.trim().matches(TIME_REGEX)) {
            throw new IllegalArgumentException(
                    fieldName + " must be in HH:mm format, got: " + value
            );
        }
        return value.trim();
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank");
        }
        return value;
    }
}
