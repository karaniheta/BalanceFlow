package com.balanceflow.balanceflow_backend.activity.service;

import com.balanceflow.balanceflow_backend.activity.dto.ActivityResponse;
import com.balanceflow.balanceflow_backend.activity.repository.ActivityRepository;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final GroupRepository groupRepository;

    public List<ActivityResponse> getGroupActivities(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return activityRepository.findByGroupOrderByCreatedAtDesc(group).stream()
                .map(a -> ActivityResponse.builder()
                        .id(a.getId())
                        .action(a.getAction())
                        .createdBy(a.getCreatedBy().getFullName())
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
