package com.balanceflow.balanceflow_backend.user.service;

import com.balanceflow.balanceflow_backend.user.dto.ChangePasswordRequest;
import com.balanceflow.balanceflow_backend.user.dto.MessageResponse;
import com.balanceflow.balanceflow_backend.user.dto.UpdateProfileRequest;
import com.balanceflow.balanceflow_backend.user.dto.UserResponse;
import com.balanceflow.balanceflow_backend.user.entity.User;
import com.balanceflow.balanceflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }

    public UserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        user.setFullName(request.getFullName());

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .build();
    }

    public MessageResponse changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        boolean matches = passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        return new MessageResponse(
                "Password changed successfully"
        );
    }
}