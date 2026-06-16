package com.balanceflow.balanceflow_backend.expense.service;

import com.balanceflow.balanceflow_backend.expense.dto.SettlementResponse;
import com.balanceflow.balanceflow_backend.expense.dto.UserBalanceDto;
import com.balanceflow.balanceflow_backend.expense.entity.Expense;
import com.balanceflow.balanceflow_backend.expense.entity.ExpenseParticipant;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseParticipantRepository;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseRepository;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.entity.GroupMember;
import com.balanceflow.balanceflow_backend.group.repository.GroupMemberRepository;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository participantRepository;

    public Map<String, UserBalanceDto> getGroupBalances(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMember> members = groupMemberRepository.findByGroup(group);
        Map<String, UserBalanceDto> balances = new HashMap<>();

        for (GroupMember member : members) {
            String name = member.getUser().getFullName();
            balances.put(name, UserBalanceDto.builder().paid(0).owes(0).net(0).build());
        }

        List<Expense> expenses = expenseRepository.findByGroup(group);
        for (Expense expense : expenses) {
            String paidBy = expense.getPaidBy().getFullName();
            UserBalanceDto payerBalance = balances.getOrDefault(paidBy, UserBalanceDto.builder().paid(0).owes(0).net(0).build());
            payerBalance.setPaid(payerBalance.getPaid() + expense.getAmount());
            balances.put(paidBy, payerBalance);

            List<ExpenseParticipant> participants = participantRepository.findByExpense(expense);
            for (ExpenseParticipant participant : participants) {
                String participantName = participant.getUser().getFullName();
                UserBalanceDto participantBalance = balances.getOrDefault(participantName, UserBalanceDto.builder().paid(0).owes(0).net(0).build());
                participantBalance.setOwes(participantBalance.getOwes() + participant.getShareAmount());
                balances.put(participantName, participantBalance);
            }
        }

        for (UserBalanceDto balance : balances.values()) {
            balance.setNet(balance.getPaid() - balance.getOwes());
        }

        return balances;
    }

    public List<SettlementResponse> getSimplifiedSettlements(UUID groupId) {
        Map<String, UserBalanceDto> balances = getGroupBalances(groupId);

        List<Map.Entry<String, Double>> debtors = new ArrayList<>();
        List<Map.Entry<String, Double>> creditors = new ArrayList<>();

        for (Map.Entry<String, UserBalanceDto> entry : balances.entrySet()) {
            double net = entry.getValue().getNet();
            if (net < -0.01) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), -net));
            } else if (net > 0.01) {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), net));
            }
        }

        debtors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        creditors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<SettlementResponse> settlements = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            Map.Entry<String, Double> debtor = debtors.get(i);
            Map.Entry<String, Double> creditor = creditors.get(j);

            double amount = Math.min(debtor.getValue(), creditor.getValue());

            // Add settlement with 2 decimal places
            double roundedAmount = Math.round(amount * 100.0) / 100.0;
            if (roundedAmount > 0) {
                settlements.add(SettlementResponse.builder()
                        .from(debtor.getKey())
                        .to(creditor.getKey())
                        .amount(roundedAmount)
                        .build());
            }

            debtor.setValue(debtor.getValue() - amount);
            creditor.setValue(creditor.getValue() - amount);

            if (debtor.getValue() < 0.01) {
                i++;
            }
            if (creditor.getValue() < 0.01) {
                j++;
            }
        }

        return settlements;
    }
}
