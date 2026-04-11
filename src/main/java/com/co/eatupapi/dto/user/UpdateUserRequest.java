package com.co.eatupapi.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.co.eatupapi.utils.user.validation.UserValidationRules;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class UpdateUserRequest {

    @NotBlank(message = "Field 'firstName' is required")
    @Size(min = UserValidationRules.NAME_MIN_LENGTH, max = UserValidationRules.NAME_MAX_LENGTH,
            message = "Field 'firstName' must be between 2 and 100 characters")
    @Pattern(regexp = UserValidationRules.NAME_REGEX,
            message = "Field 'firstName' contains invalid characters")
    private String firstName;

    @NotBlank(message = "Field 'lastName' is required")
    @Size(min = UserValidationRules.NAME_MIN_LENGTH, max = UserValidationRules.NAME_MAX_LENGTH,
            message = "Field 'lastName' must be between 2 and 100 characters")
    @Pattern(regexp = UserValidationRules.NAME_REGEX,
            message = "Field 'lastName' contains invalid characters")
    private String lastName;

    @NotNull(message = "Field 'documentTypeId' is required")
    private UUID documentTypeId;

    @NotBlank(message = "Field 'documentNumber' is required")
    @Size(min = UserValidationRules.DOCUMENT_NUMBER_MIN_LENGTH,
            max = UserValidationRules.DOCUMENT_NUMBER_MAX_LENGTH,
            message = "Field 'documentNumber' must be between 5 and 30 characters")
    @Pattern(regexp = UserValidationRules.DOCUMENT_NUMBER_REGEX,
            message = "Field 'documentNumber' contains invalid characters")
    private String documentNumber;

    @NotBlank(message = "Field 'phone' is required")
    @Pattern(regexp = UserValidationRules.PHONE_REGEX,
            message = "Field 'phone' must contain exactly 10 digits")
    private String phone;

    @Null(message = "Field 'email' cannot be modified")
    @Size(max = UserValidationRules.EMAIL_MAX_LENGTH, message = "Field 'email' must be at most 150 characters")
    private String email;

    @NotNull(message = "Field 'birthDate' is required")
    @Past(message = "Field 'birthDate' must be a past date")
    private LocalDate birthDate;

    @NotNull(message = "Field 'departmentId' is required")
    private UUID departmentId;

    @NotNull(message = "Field 'cityId' is required")
    private UUID cityId;

    @NotBlank(message = "Field 'address' is required")
    @Size(min = UserValidationRules.ADDRESS_MIN_LENGTH, max = UserValidationRules.ADDRESS_MAX_LENGTH,
            message = "Field 'address' must be between 5 and 255 characters")
    @Pattern(regexp = UserValidationRules.ADDRESS_REGEX,
            message = "Field 'address' contains invalid characters")
    private String address;

    @NotNull(message = "Field 'locationId' is required")
    private UUID locationId;

}
