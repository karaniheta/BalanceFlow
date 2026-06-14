package com.balanceflow.balanceflow_backend.group.repository;

import com.balanceflow.balanceflow_backend.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository
        extends JpaRepository<Group, Long> {
}