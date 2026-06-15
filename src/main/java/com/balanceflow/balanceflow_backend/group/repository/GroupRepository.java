package com.balanceflow.balanceflow_backend.group.repository;

import com.balanceflow.balanceflow_backend.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository
        extends JpaRepository<Group, UUID> {
}