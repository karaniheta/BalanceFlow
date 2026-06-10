package com.balanceflow.balanceflow_backend.transaction.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTransactionRequest {

    private Double amount;

    private String note;

    private LocalDate transactionDate;

    private Long categoryId;
}