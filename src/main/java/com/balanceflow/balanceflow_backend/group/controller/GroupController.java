package com.balanceflow.balanceflow_backend.group.controller;

import com.balanceflow.balanceflow_backend.group.dto.AddMemberRequest;
import com.balanceflow.balanceflow_backend.group.dto.CreateGroupRequest;
import com.balanceflow.balanceflow_backend.group.dto.GroupResponse;
import com.balanceflow.balanceflow_backend.group.service.GroupService;
import com.balanceflow.balanceflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final JwtService jwtService;

    @PostMapping
    public GroupResponse create(
            @RequestBody CreateGroupRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token =
                authHeader.replace("Bearer ", "");

        String email =
                jwtService.extractEmail(token);

        return groupService.create(
                request,
                email
        );
    }

    @PostMapping("/{groupId}/members")
    public String addMember(
            @PathVariable Long groupId,
            @RequestBody AddMemberRequest request
    ) {

        return groupService.addMember(
                groupId,
                request.getEmail()
        );
    }
}