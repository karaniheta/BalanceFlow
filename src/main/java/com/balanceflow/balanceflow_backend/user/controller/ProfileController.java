package com.balanceflow.balanceflow_backend.user.controller;

import com.balanceflow.balanceflow_backend.security.JwtService;
import com.balanceflow.balanceflow_backend.user.dto.ProfileResponse;
import com.balanceflow.balanceflow_backend.user.dto.UpdateProfileRequest;
import com.balanceflow.balanceflow_backend.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    @GetMapping
    public ProfileResponse getProfile(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        return profileService.getProfile(email);
    }

    @PatchMapping
    public ProfileResponse updateProfile(
            @RequestBody UpdateProfileRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        return profileService.updateProfile(email, request);
    }
}
