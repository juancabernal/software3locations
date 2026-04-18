package com.co.eatupapi.domain.inventory.location;

import com.co.eatupapi.utils.inventory.location.validation.LocationValidator;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

public class LocationDomain {
    private UUID id;
    private String name;
    private String city;
    private String address;
    @Setter
    private boolean active;
    private String email;
    private String phoneNumber;
    private LocalTime startTime;
    private LocalTime endTime;

    public LocationDomain(final UUID id, final String name, String city, final String address, final boolean active, final String email, final String phoneNumber, final LocalTime startTime, final LocalTime endTime) {
        setId(id);
        setName(name);
        setCity(city);
        setAddress(address);
        setActive(active);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = LocationValidator.validateName(name);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = LocationValidator.validateCity(city);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = LocationValidator.validateAddress(address);
    }

    public boolean isActive() {
        return active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = LocationValidator.validateEmail(email);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = LocationValidator.validatePhoneNumber(phoneNumber);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = LocationValidator.validateStartTime(startTime);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = LocationValidator.validateEndTime(endTime);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = LocationValidator.validateId(id);
    }
}
