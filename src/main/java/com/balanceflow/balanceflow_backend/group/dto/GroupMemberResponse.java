package com.balanceflow.balanceflow_backend.group.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GroupMemberResponse {

    private UUID id;
    private String fullName;
    private String email;
    private String role;
}