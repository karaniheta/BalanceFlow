package com.balanceflow.balanceflow_backend.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardSummaryResponse {
    private int totalGroups;
    private int totalExpenses;
    private double totalSpent;
    private double totalOwed;
}
