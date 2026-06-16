package com.balanceflow.balanceflow_backend.expense.controller;

import com.balanceflow.balanceflow_backend.expense.dto.CreateExpenseRequest;
import com.balanceflow.balanceflow_backend.expense.dto.ExpenseDetailsResponse;
import com.balanceflow.balanceflow_backend.expense.dto.ExpenseResponse;
import com.balanceflow.balanceflow_backend.expense.dto.UpdateExpenseRequest;
import com.balanceflow.balanceflow_backend.expense.service.ExpenseService;
import com.balanceflow.balanceflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final JwtService jwtService;

    @PostMapping
    public ExpenseResponse createExpense(
            @PathVariable UUID groupId,
            @RequestBody CreateExpenseRequest request,
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.replace("Bearer ", "");

        String email =
                jwtService.extractEmail(token);

        return expenseService.createExpense(
                groupId,
                request,
                email
        );
    }

    @GetMapping
    public List<ExpenseResponse> getExpenses(
            @PathVariable UUID groupId
    ) {

        return expenseService.getExpenses(
                groupId
        );
    }

    @PatchMapping("/{expenseId}")
    public ExpenseResponse updateExpense(
            @PathVariable UUID expenseId,
            @RequestBody UpdateExpenseRequest request
    ) {

        return expenseService.updateExpense(
                expenseId,
                request
        );
    }

    @GetMapping("/{expenseId}")
    public ExpenseDetailsResponse getExpenseDetails(
            @PathVariable UUID expenseId
    ) {

        return expenseService.getExpenseDetails(
                expenseId
        );
    }

    @DeleteMapping("/{expenseId}")
    public String deleteExpense(
            @PathVariable UUID expenseId
    ) {

        return expenseService.deleteExpense(
                expenseId
        );
    }
}