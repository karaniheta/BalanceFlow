package com.balanceflow.balanceflow_backend.expense.dto;

import lombok.Data;

@Data
public class CreateExpenseRequest {

    private String title;

    private Double amount;
}