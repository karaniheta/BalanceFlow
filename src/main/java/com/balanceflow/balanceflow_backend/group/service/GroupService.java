package com.balanceflow.balanceflow_backend.group.service;

import com.balanceflow.balanceflow_backend.group.dto.CreateGroupRequest;
import com.balanceflow.balanceflow_backend.group.dto.GroupResponse;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.entity.GroupMember;
import com.balanceflow.balanceflow_backend.group.entity.GroupRole;
import com.balanceflow.balanceflow_backend.group.repository.GroupMemberRepository;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupResponse create(
            CreateGroupRequest request,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

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

        return GroupResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .createdBy(saved.getCreatedBy().getFullName())
                .build();
    }

    public String addMember(
            Long groupId,
            String email
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean alreadyExists =
                groupMemberRepository
                        .findByGroupAndUser(group, user)
                        .isPresent();

        if (alreadyExists) {
            throw new RuntimeException(
                    "User already exists in group"
            );
        }

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(GroupRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();

        groupMemberRepository.save(member);

        return "Member added successfully";
    }
}