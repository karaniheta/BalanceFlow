package com.balanceflow.balanceflow_backend.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;

    private String fullName;

    private String email;
}