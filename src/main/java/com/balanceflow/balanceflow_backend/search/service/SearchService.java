package com.balanceflow.balanceflow_backend.search.service;

import com.balanceflow.balanceflow_backend.group.repository.GroupRepository;
import com.balanceflow.balanceflow_backend.search.dto.SearchGroupResponse;
import com.balanceflow.balanceflow_backend.search.dto.SearchUserResponse;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public List<SearchGroupResponse> searchGroups(String query) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getName().toLowerCase().contains(query.toLowerCase()))
                .map(g -> SearchGroupResponse.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .description(g.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public List<SearchUserResponse> searchUsers(String query) {
        return userRepository.findAll().stream()
                .filter(u -> u.getFullName().toLowerCase().contains(query.toLowerCase()) || 
                             u.getEmail().toLowerCase().contains(query.toLowerCase()))
                .map(u -> SearchUserResponse.builder()
                        .id(u.getId())
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .build())
                .collect(Collectors.toList());
    }
}
