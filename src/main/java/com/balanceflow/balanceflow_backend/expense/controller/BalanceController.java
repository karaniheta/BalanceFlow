package com.balanceflow.balanceflow_backend.expense.controller;

import com.balanceflow.balanceflow_backend.expense.dto.SettlementResponse;
import com.balanceflow.balanceflow_backend.expense.dto.UserBalanceDto;
import com.balanceflow.balanceflow_backend.expense.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/balances")
    public Map<String, UserBalanceDto> getGroupBalances(@PathVariable UUID groupId) {
        return balanceService.getGroupBalances(groupId);
    }

    @GetMapping("/settlements")
    public List<SettlementResponse> getSimplifiedSettlements(@PathVariable UUID groupId) {
        return balanceService.getSimplifiedSettlements(groupId);
    }
}
