package com.balanceflow.balanceflow_backend.transaction.repository;

import com.balanceflow.balanceflow_backend.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import java.util.List;
public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUserId(UUID userId);

    List<Transaction> findByUserIdAndCategoryId(
            UUID userId,
            UUID categoryId
    );
}