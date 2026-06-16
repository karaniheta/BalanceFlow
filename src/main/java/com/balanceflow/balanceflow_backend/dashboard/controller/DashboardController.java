package com.balanceflow.balanceflow_backend.dashboard.controller;

import com.balanceflow.balanceflow_backend.dashboard.dto.AdminDashboardStatsResponse;
import com.balanceflow.balanceflow_backend.dashboard.dto.DashboardSummaryResponse;
import com.balanceflow.balanceflow_backend.dashboard.service.DashboardService;
import com.balanceflow.balanceflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtService jwtService;

    @GetMapping
    public DashboardSummaryResponse getDashboardSummary(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        
        return dashboardService.getDashboardSummary(email);
    }

    @GetMapping("/stats")
    public AdminDashboardStatsResponse getAdminStats() {
        return dashboardService.getAdminStats();
    }
}
