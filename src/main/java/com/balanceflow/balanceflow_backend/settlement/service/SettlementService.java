package com.balanceflow.balanceflow_backend.settlement.service;

import com.balanceflow.balanceflow_backend.activity.entity.Activity;
import com.balanceflow.balanceflow_backend.activity.repository.ActivityRepository;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.repository.GroupMemberRepository;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.notification.entity.Notification;
import com.balanceflow.balanceflow_backend.notification.repository.NotificationRepository;
import com.balanceflow.balanceflow_backend.settlement.dto.CreateSettlementRequest;
import com.balanceflow.balanceflow_backend.settlement.dto.SettlementResponseDto;
import com.balanceflow.balanceflow_backend.settlement.entity.Settlement;
import com.balanceflow.balanceflow_backend.settlement.repository.SettlementRepository;
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
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final NotificationRepository notificationRepository;
    private final GroupMemberRepository groupMemberRepository;

    public SettlementResponseDto createSettlement(UUID groupId, CreateSettlementRequest request, String email) {
        
        // 1. Validate DTO fields
        if (request.getPayerId() == null || request.getReceiverId() == null || request.getAmount() == null) {
            throw new IllegalArgumentException("payerId, receiverId, and amount must not be null.");
        }
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Settlement amount must be greater than 0.");
        }
        if (request.getPayerId().equals(request.getReceiverId())) {
            throw new IllegalArgumentException("Payer and receiver cannot be the same user.");
        }

        // 2. Validate Group
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        // 3. Validate Payer
        User payer = userRepository.findById(request.getPayerId())
                .orElseThrow(() -> new RuntimeException("Payer user not found with ID: " + request.getPayerId()));

        // 4. Validate Receiver
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver user not found with ID: " + request.getReceiverId()));

        // 5. Validate Group Memberships
        boolean payerInGroup = groupMemberRepository.findByGroupAndUser(group, payer).isPresent();
        if (!payerInGroup) {
            throw new RuntimeException("Payer does not belong to the specified group.");
        }

        boolean receiverInGroup = groupMemberRepository.findByGroupAndUser(group, receiver).isPresent();
        if (!receiverInGroup) {
            throw new RuntimeException("Receiver does not belong to the specified group.");
        }

        // 6. Validate CreatedBy (Current User)
        User createdBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

        // 7. Create Settlement
        Settlement settlement = Settlement.builder()
                .group(group)
                .payer(payer)
                .receiver(receiver)
                .amount(request.getAmount())
                .settledAt(LocalDateTime.now())
                .build();

        Settlement saved = settlementRepository.save(settlement);

        // 8. Log Activity
        activityRepository.save(Activity.builder()
                .group(group)
                .action("Settlement Done: " + payer.getFullName() + " paid " + receiver.getFullName() + " an amount of " + request.getAmount())
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .build());

        // 9. Send Notification
        notificationRepository.save(Notification.builder()
                .user(receiver)
                .title("New Settlement")
                .message(payer.getFullName() + " paid you " + request.getAmount() + " in group '" + group.getName() + "'")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build());

        return mapToDto(saved);
    }

    public List<SettlementResponseDto> getSettlementHistory(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return settlementRepository.findByGroup(group).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SettlementResponseDto mapToDto(Settlement s) {
        return SettlementResponseDto.builder()
                .id(s.getId())
                .payer(s.getPayer().getFullName())
                .receiver(s.getReceiver().getFullName())
                .amount(s.getAmount())
                .settledAt(s.getSettledAt())
                .build();
    }
}
