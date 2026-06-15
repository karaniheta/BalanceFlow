package com.balanceflow.balanceflow_backend.group.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MyGroupResponse {

    private UUID id;

    private String name;

    private String description;

    private String role;
}