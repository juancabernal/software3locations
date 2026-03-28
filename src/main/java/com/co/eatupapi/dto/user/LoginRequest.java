package com.co.eatupapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Field 'email' is required")
    @Email(message = "Field 'email' must contain a valid email address")
    @Size(max = 150, message = "Field 'email' must be at most 150 characters")
    private String email;

    @NotBlank(message = "Field 'password' is required")
    private String password;

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
