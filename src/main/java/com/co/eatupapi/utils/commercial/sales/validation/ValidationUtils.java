package com.co.eatupapi.utils.commercial.sales.validation;

import com.co.eatupapi.utils.commercial.sales.exceptions.SaleValidationException;
import java.math.BigDecimal;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void requireObject(Object value, String message) {
        if (value == null) {
            throw new SaleValidationException(message);
        }
    }

    public static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new SaleValidationException("El campo '" + fieldName + "' es obligatorio y no puede estar vacío.");
        }
    }

    public static void requirePositive(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SaleValidationException(message);
        }
    }

    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new SaleValidationException(
                    "El campo '" + fieldName + "' no puede superar " + maxLength + " caracteres."
            );
        }
    }
}
