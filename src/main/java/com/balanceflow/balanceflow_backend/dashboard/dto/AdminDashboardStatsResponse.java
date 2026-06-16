package com.balanceflow.balanceflow_backend.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardStatsResponse {
    private long totalGroups;
    private long totalExpenses;
    private long totalSettlements;
    private long totalUsers;
}
