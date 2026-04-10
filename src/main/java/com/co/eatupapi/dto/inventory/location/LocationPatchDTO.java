package com.co.eatupapi.dto.inventory.location;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Actualización parcial de una sede.
 * Solo los campos no nulos se aplican; el resto se conserva.
 */
public class LocationPatchDTO {

    @Size(max = 100, message = "name must not exceed 100 characters")
    private String name;

    private String city;

    private String address;

    private Boolean active;

    @Email(message = "email is not valid")
    private String email;

    @Pattern(regexp = "^\\+?\\d{7,15}$", message = "phoneNumber is not valid")
    private String phoneNumber;

    private String startTime;

    private String endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
