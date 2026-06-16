package com.balanceflow.balanceflow_backend.group.service;

import com.balanceflow.balanceflow_backend.activity.entity.Activity;
import com.balanceflow.balanceflow_backend.activity.repository.ActivityRepository;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseParticipantRepository;
import com.balanceflow.balanceflow_backend.expense.repository.ExpenseRepository;
import com.balanceflow.balanceflow_backend.group.dto.CreateGroupRequest;
import com.balanceflow.balanceflow_backend.group.dto.GroupDetailsResponse;
import com.balanceflow.balanceflow_backend.group.dto.GroupMemberResponse;
import com.balanceflow.balanceflow_backend.group.dto.GroupResponse;
import com.balanceflow.balanceflow_backend.group.dto.MyGroupResponse;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.entity.GroupMember;
import com.balanceflow.balanceflow_backend.group.entity.GroupRole;
import com.balanceflow.balanceflow_backend.group.repository.GroupMemberRepository;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.notification.entity.Notification;
import com.balanceflow.balanceflow_backend.notification.repository.NotificationRepository;
import com.balanceflow.balanceflow_backend.settlement.repository.SettlementRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ActivityRepository activityRepository;
    private final NotificationRepository notificationRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;
    private final SettlementRepository settlementRepository;

    public GroupResponse create(CreateGroupRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        Group saved = groupRepository.save(group);

        GroupMember ownerMember = GroupMember.builder()
                .group(saved)
                .user(user)
                .role(GroupRole.OWNER)
                .joinedAt(LocalDateTime.now())
                .build();

        groupMemberRepository.save(ownerMember);

        activityRepository.save(Activity.builder()
                .group(saved)
                .action("Group Created")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build());

        return GroupResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .createdBy(saved.getCreatedBy().getFullName())
                .build();
    }

    public String addMember(UUID groupId, String email) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyExists = groupMemberRepository.findByGroupAndUser(group, user).isPresent();
        if (alreadyExists) {
            throw new RuntimeException("User already exists in group");
        }

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(GroupRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();

        groupMemberRepository.save(member);

        activityRepository.save(Activity.builder()
                .group(group)
                .action("Member Added: " + user.getFullName())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build());

        notificationRepository.save(Notification.builder()
                .user(user)
                .title("Added to Group")
                .message("You were added to group: " + group.getName())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build());

        return "Member added successfully";
    }

    public List<MyGroupResponse> getMyGroups(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return groupMemberRepository.findByUser(user).stream()
                .map(groupMember -> MyGroupResponse.builder()
                        .id(groupMember.getGroup().getId())
                        .name(groupMember.getGroup().getName())
                        .description(groupMember.getGroup().getDescription())
                        .role(groupMember.getRole().name())
                        .build())
                .toList();
    }

    public GroupDetailsResponse getGroup(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        int memberCount = groupMemberRepository.findByGroup(group).size();
        int totalExpenses = expenseRepository.findByGroup(group).size();

        return GroupDetailsResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .memberCount(memberCount)
                .totalExpenses(totalExpenses)
                .createdBy(group.getCreatedBy().getFullName())
                .build();
    }

    public List<GroupMemberResponse> getMembers(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return groupMemberRepository.findByGroup(group).stream()
                .map(member -> GroupMemberResponse.builder()
                        .id(member.getUser().getId())
                        .fullName(member.getUser().getFullName())
                        .email(member.getUser().getEmail())
                        .role(member.getRole().name())
                        .build())
                .toList();
    }

    public String removeMember(UUID groupId, UUID userId, String requesterEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        GroupMember requesterMember = groupMemberRepository.findByGroupAndUser(group, requester)
                .orElseThrow(() -> new RuntimeException("Requester is not in the group"));

        if (requesterMember.getRole() != GroupRole.OWNER) {
            throw new RuntimeException("Only OWNER can remove members");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        groupMemberRepository.delete(member);
        return "Member removed successfully";
    }

    public String leaveGroup(UUID groupId, String email) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<GroupMember> allMembers = groupMemberRepository.findByGroup(group);

        if (member.getRole() == GroupRole.OWNER) {
            if (allMembers.size() > 1) {
                throw new RuntimeException("OWNER cannot leave if members still exist");
            }
        }

        groupMemberRepository.delete(member);
        return "Left group successfully";
    }

    public String deleteGroup(UUID groupId, String requesterEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        GroupMember requesterMember = groupMemberRepository.findByGroupAndUser(group, requester)
                .orElseThrow(() -> new RuntimeException("Requester is not in the group"));

        if (requesterMember.getRole() != GroupRole.OWNER) {
            throw new RuntimeException("Only OWNER can delete group");
        }

        // Cascade delete
        expenseRepository.findByGroup(group).forEach(e -> expenseParticipantRepository.deleteByExpense(e));
        expenseRepository.deleteByGroup(group);
        settlementRepository.deleteByGroup(group);
        activityRepository.deleteByGroup(group);
        groupMemberRepository.deleteByGroup(group);
        groupRepository.delete(group);

        return "Group deleted successfully";
    }
}