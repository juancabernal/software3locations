package com.co.eatupapi.dto.user;

import com.co.eatupapi.utils.user.validation.UserValidationRules;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NotBlank(message = "Field 'email' is required")
    @Size(max = UserValidationRules.EMAIL_MAX_LENGTH, message = "Field 'email' must be at most 150 characters")
    @Pattern(regexp = UserValidationRules.EMAIL_REGEX,
            message = "Field 'email' must contain a valid email address")
    private String email;

    @NotBlank(message = "Field 'password' is required")
    @Size(max = UserValidationRules.PASSWORD_MAX_LENGTH,
            message = "Field 'password' must be at most 72 characters")
    private String password;

}
