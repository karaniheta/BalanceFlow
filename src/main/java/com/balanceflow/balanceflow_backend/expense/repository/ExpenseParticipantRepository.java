package com.balanceflow.balanceflow_backend.expense.repository;

import com.balanceflow.balanceflow_backend.expense.entity.Expense;
import com.balanceflow.balanceflow_backend.expense.entity.ExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseParticipantRepository
        extends JpaRepository<ExpenseParticipant, UUID> {

    List<ExpenseParticipant> findByExpense(
            Expense expense
    );
    void deleteByExpense(
            Expense expense
    );
}