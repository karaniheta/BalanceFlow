package com.balanceflow.balanceflow_backend.expense.entity;

import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private LocalDateTime createdAt;
}