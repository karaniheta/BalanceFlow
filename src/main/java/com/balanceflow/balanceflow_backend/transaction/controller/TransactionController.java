package com.balanceflow.balanceflow_backend.transaction.controller;

import com.balanceflow.balanceflow_backend.security.JwtService;
import com.balanceflow.balanceflow_backend.transaction.dto.CreateTransactionRequest;
import com.balanceflow.balanceflow_backend.transaction.dto.TransactionResponse;
import com.balanceflow.balanceflow_backend.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.balanceflow.balanceflow_backend.transaction.dto.DeleteResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtService jwtService;

    @PostMapping
    public TransactionResponse create(
            @RequestBody CreateTransactionRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        return transactionService.create(request, email);
    }

    @GetMapping
    public List<TransactionResponse> getTransactions(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) UUID categoryId
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        if (categoryId != null) {
            return transactionService.getByCategory(
                    email,
                    categoryId
            );
        }

        return transactionService.getAll(email);
    }
    @PutMapping("/{id}")
    public TransactionResponse update(
            @PathVariable UUID id,
            @RequestBody CreateTransactionRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        return transactionService.update(
                id,
                request,
                email
        );
    }

    @DeleteMapping("/{id}")
    public DeleteResponse delete(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);

        return transactionService.delete(
                id,
                email
        );
    }
}