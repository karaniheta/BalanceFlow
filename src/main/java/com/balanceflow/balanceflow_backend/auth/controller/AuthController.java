package com.balanceflow.balanceflow_backend.auth.controller;

import com.balanceflow.balanceflow_backend.auth.dto.*;
import com.balanceflow.balanceflow_backend.auth.service.AuthService;
import com.balanceflow.balanceflow_backend.auth.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final OtpService otpService;
    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/send-otp")
    public OtpResponse sendOtp(
            @RequestBody SendOtpRequest request
    ) {
        return otpService.sendOtp(
                request.getEmail()
        );
    }

    @PostMapping("/verify-otp")
    public OtpResponse verifyOtp(
            @RequestBody VerifyOtpRequest request
    ) {
        return otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );
    }

    @PostMapping("/reset-password")
    public OtpResponse resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        return authService.resetPassword(request);
    }
}