package com.balanceflow.balanceflow_backend.expense.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBalanceDto {
    private double paid;
    private double owes;
    private double net;
}
