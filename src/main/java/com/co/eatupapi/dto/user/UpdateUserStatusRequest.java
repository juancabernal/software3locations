package com.co.eatupapi.dto.user;

import jakarta.validation.constraints.NotBlank;
public class UpdateUserStatusRequest {

    @NotBlank(message = "Field 'status' is required and cannot be empty")
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
