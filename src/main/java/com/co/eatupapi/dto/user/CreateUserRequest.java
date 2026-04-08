package com.co.eatupapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class CreateUserRequest {

    @NotBlank(message = "Field 'firstName' is required")
    @Size(max = 100, message = "Field 'firstName' must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Field 'lastName' is required")
    @Size(max = 100, message = "Field 'lastName' must be at most 100 characters")
    private String lastName;

    @NotNull(message = "Field 'documentTypeId' is required")
    private UUID documentTypeId;

    @NotBlank(message = "Field 'documentNumber' is required")
    @Size(min = 5, max = 30, message = "Field 'documentNumber' must be between 5 and 30 characters")
    private String documentNumber;

    @NotBlank(message = "Field 'phone' is required")
    @Pattern(regexp = "^\\d{10}$", message = "Field 'phone' must contain exactly 10 digits")
    private String phone;

    @NotBlank(message = "Field 'email' is required")
    @Email(message = "Field 'email' must contain a valid email address")
    @Size(max = 150, message = "Field 'email' must be at most 150 characters")
    private String email;

    @NotBlank(message = "Field 'password' is required")
    @Size(min = 8, max = 72, message = "Field 'password' must be between 8 and 72 characters")
    private String password;

    @NotNull(message = "Field 'birthDate' is required")
    @Past(message = "Field 'birthDate' must be a past date")
    private LocalDate birthDate;

    @NotNull(message = "Field 'departmentId' is required")
    private UUID departmentId;

    @NotNull(message = "Field 'cityId' is required")
    private UUID cityId;

    @NotBlank(message = "Field 'address' is required")
    @Size(min = 5, max = 255, message = "Field 'address' must be between 5 and 255 characters")
    private String address;

    private UUID locationId;

}
