package com.balanceflow.balanceflow_backend.activity.controller;

import com.balanceflow.balanceflow_backend.activity.dto.ActivityResponse;
import com.balanceflow.balanceflow_backend.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public List<ActivityResponse> getGroupActivities(@PathVariable UUID groupId) {
        return activityService.getGroupActivities(groupId);
    }
}
