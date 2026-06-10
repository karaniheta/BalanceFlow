package com.balanceflow.balanceflow_backend.auth.controller;

import com.balanceflow.balanceflow_backend.auth.dto.LoginRequest;
import com.balanceflow.balanceflow_backend.auth.dto.LoginResponse;
import com.balanceflow.balanceflow_backend.auth.dto.RegisterRequest;
import com.balanceflow.balanceflow_backend.auth.dto.RegisterResponse;
import com.balanceflow.balanceflow_backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

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
}