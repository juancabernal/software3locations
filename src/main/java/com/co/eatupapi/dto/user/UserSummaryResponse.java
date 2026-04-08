package com.co.eatupapi.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserSummaryResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String email;
    private String phone;
    private String location;
    private String status;

}
