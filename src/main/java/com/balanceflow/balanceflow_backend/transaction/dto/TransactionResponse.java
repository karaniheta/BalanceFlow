package com.balanceflow.balanceflow_backend.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {

    private Long id;

    private Double amount;

    private String note;

    private LocalDate transactionDate;

    private String categoryName;

    private String categoryType;
}