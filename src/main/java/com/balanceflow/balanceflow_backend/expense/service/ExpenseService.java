package com.balanceflow.balanceflow_backend.expense.service;

import com.balanceflow.balanceflow_backend.activity.entity.Activity;
import com.balanceflow.balanceflow_backend.activity.repository.ActivityRepository;
import com.balanceflow.balanceflow_backend.expense.dto.CreateExpenseRequest;
import com.balanceflow.balanceflow_backend.expense.dto.ExpenseDetailsResponse;
import com.balanceflow.balanceflow_backend.expense.dto.ExpenseParticipantResponse;
import com.balanceflow.balanceflow_backend.expense.dto.ExpenseResponse;
import com.balanceflow.balanceflow_backend.expense.dto.UpdateExpenseRequest;
import com.balanceflow.balanceflow_backend.expense.entity.Expense;
import com.balanceflow.balanceflow_backend.expense.entity.ExpenseParticipant;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseParticipantRepository;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseRepository;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.entity.GroupMember;
import com.balanceflow.balanceflow_backend.group.repository.GroupMemberRepository;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.notification.entity.Notification;
import com.balanceflow.balanceflow_backend.notification.repository.NotificationRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository participantRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final NotificationRepository notificationRepository;

    public ExpenseResponse createExpense(
            UUID groupId,
            CreateExpenseRequest request,
            String email
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        User paidBy = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .group(group)
                .paidBy(paidBy)
                .createdAt(LocalDateTime.now())
                .build();

        Expense savedExpense =
                expenseRepository.save(expense);

        List<GroupMember> members =
                groupMemberRepository.findByGroup(group);

        double share =
                request.getAmount() / members.size();

        for (GroupMember member : members) {

            ExpenseParticipant participant =
                    ExpenseParticipant.builder()
                            .expense(savedExpense)
                            .user(member.getUser())
                            .shareAmount(share)
                            .build();

            participantRepository.save(participant);

            if (!member.getUser().getId().equals(paidBy.getId())) {
                notificationRepository.save(Notification.builder()
                        .user(member.getUser())
                        .title("New Expense")
                        .message("You were added to expense: " + expense.getTitle() + " in group " + group.getName())
                        .isRead(false)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }

        activityRepository.save(Activity.builder()
                .group(group)
                .action("Expense Added: " + savedExpense.getTitle() + " by " + paidBy.getFullName())
                .createdBy(paidBy)
                .createdAt(LocalDateTime.now())
                .build());

        List<ExpenseParticipantResponse> participants =
                participantRepository.findByExpense(savedExpense)
                        .stream()
                        .map(p -> ExpenseParticipantResponse.builder()
                                .fullName(
                                        p.getUser().getFullName()
                                )
                                .shareAmount(
                                        p.getShareAmount()
                                )
                                .build())
                        .collect(Collectors.toList());

        return ExpenseResponse.builder()
                .id(savedExpense.getId())
                .title(savedExpense.getTitle())
                .amount(savedExpense.getAmount())
                .paidBy(savedExpense.getPaidBy().getFullName())
                .participants(participants)
                .build();
    }

    public List<ExpenseResponse> getExpenses(
            UUID groupId
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        List<Expense> expenses =
                expenseRepository.findByGroup(group);

        return expenses.stream()
                .map(expense -> {

                    List<ExpenseParticipantResponse>
                            participants =
                            participantRepository
                                    .findByExpense(expense)
                                    .stream()
                                    .map(p ->
                                            ExpenseParticipantResponse
                                                    .builder()
                                                    .fullName(
                                                            p.getUser()
                                                                    .getFullName()
                                                    )
                                                    .shareAmount(
                                                            p.getShareAmount()
                                                    )
                                                    .build()
                                    )
                                    .collect(Collectors.toList());

                    return ExpenseResponse.builder()
                            .id(expense.getId())
                            .title(expense.getTitle())
                            .amount(expense.getAmount())
                            .paidBy(
                                    expense.getPaidBy()
                                            .getFullName()
                            )
                            .participants(participants)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public String deleteExpense(
            UUID expenseId
    ) {

        Expense expense =
                expenseRepository.findById(expenseId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Expense not found"
                                ));

        participantRepository.deleteByExpense(
                expense
        );

        expenseRepository.delete(
                expense
        );

        activityRepository.save(Activity.builder()
                .group(expense.getGroup())
                .action("Expense Deleted: " + expense.getTitle())
                .createdBy(expense.getPaidBy())
                .createdAt(LocalDateTime.now())
                .build());

        return "Expense deleted successfully";
    }

    public ExpenseResponse updateExpense(
            UUID expenseId,
            UpdateExpenseRequest request
    ) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (request.getTitle() != null) {
            expense.setTitle(request.getTitle());
        }

        if (request.getAmount() != null && !request.getAmount().equals(expense.getAmount())) {
            expense.setAmount(request.getAmount());

            List<ExpenseParticipant> participants = participantRepository.findByExpense(expense);
            double newShare = request.getAmount() / participants.size();

            for (ExpenseParticipant participant : participants) {
                participant.setShareAmount(newShare);
                participantRepository.save(participant);
            }
        }

        Expense updatedExpense = expenseRepository.save(expense);

        activityRepository.save(Activity.builder()
                .group(updatedExpense.getGroup())
                .action("Expense Updated: " + updatedExpense.getTitle())
                .createdBy(updatedExpense.getPaidBy())
                .createdAt(LocalDateTime.now())
                .build());

        List<ExpenseParticipantResponse> participantResponses =
                participantRepository.findByExpense(updatedExpense)
                        .stream()
                        .map(p -> ExpenseParticipantResponse.builder()
                                .fullName(p.getUser().getFullName())
                                .shareAmount(p.getShareAmount())
                                .build())
                        .collect(Collectors.toList());

        return ExpenseResponse.builder()
                .id(updatedExpense.getId())
                .title(updatedExpense.getTitle())
                .amount(updatedExpense.getAmount())
                .paidBy(updatedExpense.getPaidBy().getFullName())
                .participants(participantResponses)
                .build();
    }

    public ExpenseDetailsResponse getExpenseDetails(UUID expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        List<ExpenseParticipantResponse> participants =
                participantRepository.findByExpense(expense)
                        .stream()
                        .map(p -> ExpenseParticipantResponse.builder()
                                .fullName(p.getUser().getFullName())
                                .shareAmount(p.getShareAmount())
                                .build())
                        .collect(Collectors.toList());

        return ExpenseDetailsResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .paidBy(expense.getPaidBy().getFullName())
                .groupId(expense.getGroup().getId())
                .groupName(expense.getGroup().getName())
                .participants(participants)
                .build();
    }
}