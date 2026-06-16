package com.balanceflow.balanceflow_backend.settlement.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SettlementResponseDto {
    private UUID id;
    private String payer;
    private String receiver;
    private Double amount;
    private LocalDateTime settledAt;
}
