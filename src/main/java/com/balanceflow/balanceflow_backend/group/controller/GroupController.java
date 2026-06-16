package com.balanceflow.balanceflow_backend.group.controller;

import com.balanceflow.balanceflow_backend.group.dto.*;
import com.balanceflow.balanceflow_backend.group.service.GroupService;
import com.balanceflow.balanceflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

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
            @PathVariable UUID groupId,
            @RequestBody AddMemberRequest request
    ) {

        return groupService.addMember(
                groupId,
                request.getEmail()
        );
    }

    @GetMapping("/test")
    public String test() {
        return "GROUP CONTROLLER WORKING";
    }


    @GetMapping(value = {"/my-groups", ""})
    public List<MyGroupResponse> getMyGroups(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        System.out.println("GET MY GROUPS HIT");

        String token =
                authHeader.replace("Bearer ", "");

        String email =
                jwtService.extractEmail(token);

        return groupService.getMyGroups(email);
    }

    @GetMapping("/{groupId}")
    public GroupDetailsResponse getGroup(
            @PathVariable UUID groupId
    ) {

        return groupService.getGroup(groupId);
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMemberResponse> getMembers(
            @PathVariable UUID groupId
    ) {

        return groupService.getMembers(groupId);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public String removeMember(
            @PathVariable UUID groupId,
            @PathVariable UUID userId,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        return groupService.removeMember(
                groupId,
                userId,
                email
        );
    }

    @PostMapping("/{groupId}/leave")
    public String leaveGroup(
            @PathVariable UUID groupId,
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.replace("Bearer ", "");

        String email =
                jwtService.extractEmail(token);

        return groupService.leaveGroup(
                groupId,
                email
        );
    }

    @DeleteMapping("/{groupId}")
    public String deleteGroup(
            @PathVariable UUID groupId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        return groupService.deleteGroup(groupId, email);
    }
}