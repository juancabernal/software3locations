package com.co.eatupapi.dto.user;

import com.co.eatupapi.utils.user.validation.UserValidationRules;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateUserStatusRequest {

    @NotBlank(message = "Field 'status' is required and cannot be empty")
    @Pattern(regexp = UserValidationRules.STATUS_REGEX,
            message = "Field 'status' must be one of: ACTIVE, INACTIVE")
    private String status;

    public UpdateUserStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
