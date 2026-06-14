package com.balanceflow.balanceflow_backend.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String email;

    private String newPassword;
}