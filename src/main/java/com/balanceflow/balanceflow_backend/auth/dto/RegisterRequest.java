package com.balanceflow.balanceflow_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}