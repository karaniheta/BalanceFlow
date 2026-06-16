package com.balanceflow.balanceflow_backend.activity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ActivityResponse {
    private UUID id;
    private String action;
    private String createdBy;
    private LocalDateTime createdAt;
}
