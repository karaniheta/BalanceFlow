package com.balanceflow.balanceflow_backend.expense.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SettlementResponse {
    private String from;
    private String to;
    private double amount;
}
