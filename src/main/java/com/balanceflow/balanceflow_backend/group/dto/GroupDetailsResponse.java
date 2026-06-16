package com.balanceflow.balanceflow_backend.group.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class GroupDetailsResponse {
    private UUID id;
    private String name;
    private String description;
    private int memberCount;
    private int totalExpenses;
    private String createdBy;
}
