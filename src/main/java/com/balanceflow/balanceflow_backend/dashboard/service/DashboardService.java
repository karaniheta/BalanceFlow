package com.balanceflow.balanceflow_backend.dashboard.service;

import com.balanceflow.balanceflow_backend.dashboard.dto.AdminDashboardStatsResponse;
import com.balanceflow.balanceflow_backend.dashboard.dto.DashboardSummaryResponse;
import com.balanceflow.balanceflow_backend.expense.entity.Expense;
import com.balanceflow.balanceflow_backend.expense.entity.ExpenseParticipant;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseParticipantRepository;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseRepository;
import com.balanceflow.balanceflow_backend.group.entity.GroupMember;
import com.balanceflow.balanceflow_backend.group.repository.GroupMemberRepository;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.settlement.repository.SettlementRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository participantRepository;
    private final SettlementRepository settlementRepository;

    public DashboardSummaryResponse getDashboardSummary(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupMember> groupMemberships = groupMemberRepository.findByUser(user);
        int totalGroups = groupMemberships.size();

        List<com.balanceflow.balanceflow_backend.group.entity.Group> userGroups = groupMemberships.stream()
                .map(GroupMember::getGroup)
                .collect(Collectors.toList());

        double totalSpent = 0;
        double totalOwed = 0;
        int totalExpenses = 0;

        for (com.balanceflow.balanceflow_backend.group.entity.Group group : userGroups) {
            List<Expense> expenses = expenseRepository.findByGroup(group);
            totalExpenses += expenses.size();

            for (Expense expense : expenses) {
                if (expense.getPaidBy().getId().equals(user.getId())) {
                    totalSpent += expense.getAmount();
                }

                List<ExpenseParticipant> participants = participantRepository.findByExpense(expense);
                for (ExpenseParticipant participant : participants) {
                    if (participant.getUser().getId().equals(user.getId()) && !expense.getPaidBy().getId().equals(user.getId())) {
                        totalOwed += participant.getShareAmount();
                    }
                }
            }
        }

        return DashboardSummaryResponse.builder()
                .totalGroups(totalGroups)
                .totalExpenses(totalExpenses)
                .totalSpent(totalSpent)
                .totalOwed(totalOwed)
                .build();
    }

    public AdminDashboardStatsResponse getAdminStats() {
        return AdminDashboardStatsResponse.builder()
                .totalGroups(groupRepository.count())
                .totalExpenses(expenseRepository.count())
                .totalSettlements(settlementRepository.count())
                .totalUsers(userRepository.count())
                .build();
    }
}
