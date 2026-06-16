package com.balanceflow.balanceflow_backend.expense.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ExpenseDetailsResponse {
    private UUID id;
    private String title;
    private Double amount;
    private String paidBy;
    private UUID groupId;
    private String groupName;
    private List<ExpenseParticipantResponse> participants;
}
