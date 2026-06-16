package com.balanceflow.balanceflow_backend.expense.dto;

import lombok.Data;

@Data
public class UpdateExpenseRequest {
    private String title;
    private Double amount;
}
