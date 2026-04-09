package com.co.eatupapi.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class UserResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private String department;
    private String city;
    private String address;
    private String location;
    private String status;

}
