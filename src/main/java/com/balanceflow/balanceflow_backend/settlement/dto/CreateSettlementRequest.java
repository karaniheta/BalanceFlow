package com.balanceflow.balanceflow_backend.settlement.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateSettlementRequest {
    private UUID payerId;
    private UUID receiverId;
    private Double amount;
}
