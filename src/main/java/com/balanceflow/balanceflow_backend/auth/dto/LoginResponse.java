package com.balanceflow.balanceflow_backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String email;

    private String token;

    private String fullName;
}