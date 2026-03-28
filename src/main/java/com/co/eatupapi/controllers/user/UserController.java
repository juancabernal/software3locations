package com.co.eatupapi.controllers.user;

import com.co.eatupapi.dto.user.CreateUserRequest;
import com.co.eatupapi.dto.user.UpdateUserRequest;
import com.co.eatupapi.dto.user.UpdateUserStatusRequest;
import com.co.eatupapi.dto.user.UserResponse;
import com.co.eatupapi.dto.user.UserSummaryResponse;
import com.co.eatupapi.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userapi/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse saved = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getUsers(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size) {
        List<UserSummaryResponse> users = userService.getUsers(status, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponse> updateStatus(@PathVariable String userId,
                                                     @RequestBody UpdateUserStatusRequest request) {
        UserResponse updated = userService.updateStatus(userId, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}
