package com.co.eatupapi.services.user;

import com.co.eatupapi.dto.user.CreateUserRequest;
import com.co.eatupapi.dto.user.UpdateUserRequest;
import com.co.eatupapi.dto.user.UserResponse;
import com.co.eatupapi.dto.user.UserSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(UUID userId);

    List<UserSummaryResponse> getUsers(String status, Integer page, Integer size);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    UserResponse updateStatus(UUID userId, String status);
}
