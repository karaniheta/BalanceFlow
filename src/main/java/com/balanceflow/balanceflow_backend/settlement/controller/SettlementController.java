package com.balanceflow.balanceflow_backend.settlement.controller;

import com.balanceflow.balanceflow_backend.security.JwtService;
import com.balanceflow.balanceflow_backend.settlement.dto.CreateSettlementRequest;
import com.balanceflow.balanceflow_backend.settlement.dto.SettlementResponseDto;
import com.balanceflow.balanceflow_backend.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;
    private final JwtService jwtService;

    @PostMapping
    public SettlementResponseDto createSettlement(
            @PathVariable UUID groupId,
            @RequestBody CreateSettlementRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        return settlementService.createSettlement(groupId, request, email);
    }

    @GetMapping("/history")
    public List<SettlementResponseDto> getSettlementHistory(@PathVariable UUID groupId) {
        return settlementService.getSettlementHistory(groupId);
    }
}
