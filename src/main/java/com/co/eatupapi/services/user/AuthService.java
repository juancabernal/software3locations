package com.co.eatupapi.services.user;

import com.co.eatupapi.dto.user.LoginRequest;
import com.co.eatupapi.dto.user.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
