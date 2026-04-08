package com.co.eatupapi.domain.inventory.location;

import com.co.eatupapi.utils.inventory.location.validation.LocationValidator;
import lombok.Setter;

public class LocationDomain {
    private String id;
    private String name;
    private String city;
    private String address;
    @Setter
    private boolean active;
    private String email;
    private String phoneNumber;
    private String startTime;
    private String endTime;

    public LocationDomain(final String id, final String name, String city, final String address, final boolean active, final String email, final String phoneNumber, final String startTime, final String endTime) {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocationValidator.validateStartTime(startTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = LocationValidator.validateEndTime(endTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = LocationValidator.validateId(id);
    }
}
