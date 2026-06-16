package com.balanceflow.balanceflow_backend.notification.service;

import com.balanceflow.balanceflow_backend.notification.dto.NotificationResponse;
import com.balanceflow.balanceflow_backend.notification.entity.Notification;
import com.balanceflow.balanceflow_backend.notification.repository.NotificationRepository;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getNotifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(UUID id, String email) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        if(!n.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        
        n.setRead(true);
        notificationRepository.save(n);
    }

    public void markAllAsRead(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        notificationRepository.markAllAsReadForUser(user);
    }

    private NotificationResponse mapToDto(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
