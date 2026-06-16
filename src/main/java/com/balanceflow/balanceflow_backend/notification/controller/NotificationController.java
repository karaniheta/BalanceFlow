package com.balanceflow.balanceflow_backend.notification.controller;

import com.balanceflow.balanceflow_backend.notification.dto.NotificationResponse;
import com.balanceflow.balanceflow_backend.notification.service.NotificationService;
import com.balanceflow.balanceflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtService jwtService;

    @GetMapping
    public List<NotificationResponse> getNotifications(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        return notificationService.getNotifications(email);
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        notificationService.markAsRead(id, email);
    }

    @PatchMapping("/read-all")
    public void markAllAsRead(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        notificationService.markAllAsRead(email);
    }
}
