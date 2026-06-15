package com.balanceflow.balanceflow_backend.group.entity;

import com.balanceflow.balanceflow_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private LocalDateTime createdAt;
}