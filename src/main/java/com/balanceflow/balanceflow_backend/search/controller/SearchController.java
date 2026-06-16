package com.balanceflow.balanceflow_backend.search.controller;

import com.balanceflow.balanceflow_backend.search.dto.SearchGroupResponse;
import com.balanceflow.balanceflow_backend.search.dto.SearchUserResponse;
import com.balanceflow.balanceflow_backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/groups")
    public List<SearchGroupResponse> searchGroups(@RequestParam("q") String query) {
        return searchService.searchGroups(query);
    }

    @GetMapping("/users")
    public List<SearchUserResponse> searchUsers(@RequestParam("q") String query) {
        return searchService.searchUsers(query);
    }
}
