package com.balanceflow.balanceflow_backend.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProfileResponse {
    private UUID id;
    private String fullName;
    private String email;
    private LocalDateTime joinedAt;
}
