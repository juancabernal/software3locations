package com.co.eatupapi.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    private String token;
    private String tokenType;
    private long expiresInSeconds;

    public LoginResponse(String token, String tokenType, long expiresInSeconds) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresInSeconds = expiresInSeconds;
    }

}
