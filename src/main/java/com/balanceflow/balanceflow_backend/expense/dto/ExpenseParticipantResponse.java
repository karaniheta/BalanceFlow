package com.balanceflow.balanceflow_backend.expense.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseParticipantResponse {

    private String fullName;

    private Double shareAmount;
}