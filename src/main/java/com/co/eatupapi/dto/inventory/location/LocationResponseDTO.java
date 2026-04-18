package com.co.eatupapi.dto.inventory.location;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;
import java.util.UUID;

public class LocationResponseDTO {
    private UUID id;
    private String name;
    private String city;
    private String address;
    private boolean active;
    private String email;
    private String phoneNumber;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public static LocationResponseDTO fromDomain(LocationDomain domain){
        LocationResponseDTO dto = new LocationResponseDTO();
        dto.id = domain.getId();
        dto.name = domain.getName();
        dto.city = domain.getCity();
        dto.address = domain.getAddress();
        dto.active = domain.isActive();
        dto.email = domain.getEmail();
        dto.phoneNumber = domain.getPhoneNumber();
        dto.startTime = domain.getStartTime();
        dto.endTime = domain.getEndTime();
        return dto;
    }

    public UUID getId() {return id;}
    public String getName() {return name;}
    public String getCity() {return city;}
    public String getAddress() {return address;}
    public boolean isActive() {return active;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public LocalTime getStartTime() {return startTime;}
    public LocalTime getEndTime() {return endTime;}
}
