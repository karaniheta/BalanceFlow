package com.balanceflow.balanceflow_backend.expense.repository;

import com.balanceflow.balanceflow_backend.expense.entity.Expense;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository
        extends JpaRepository<Expense, UUID> {

    List<Expense> findByGroup(Group group);
    void deleteByGroup(Group group);
}