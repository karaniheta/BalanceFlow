package com.balanceflow.balanceflow_backend.user.controller;

import com.balanceflow.balanceflow_backend.security.JwtService;
import com.balanceflow.balanceflow_backend.user.dto.ChangePasswordRequest;
import com.balanceflow.balanceflow_backend.user.dto.MessageResponse;
import com.balanceflow.balanceflow_backend.user.dto.UpdateProfileRequest;
import com.balanceflow.balanceflow_backend.user.dto.UserResponse;
import com.balanceflow.balanceflow_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/profile")
    public UserResponse getProfile(
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        return userService.getProfile(email);
    }

    @PutMapping("/profile")
    public UserResponse updateProfile(
            @RequestBody UpdateProfileRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        return userService.updateProfile(
                email,
                request
        );
    }

    @PostMapping("/change-password")
    public MessageResponse changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        return userService.changePassword(
                email,
                request
        );
    }
}