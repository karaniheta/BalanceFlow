package com.balanceflow.balanceflow_backend.user.service;

import com.balanceflow.balanceflow_backend.user.dto.ProfileResponse;
import com.balanceflow.balanceflow_backend.user.dto.UpdateProfileRequest;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .joinedAt(user.getCreatedAt())
                .build();
    }

    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        
        userRepository.save(user);

        return ProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .joinedAt(user.getCreatedAt())
                .build();
    }
}
