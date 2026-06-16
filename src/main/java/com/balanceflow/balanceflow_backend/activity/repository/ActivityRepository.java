package com.balanceflow.balanceflow_backend.activity.repository;

import com.balanceflow.balanceflow_backend.activity.entity.Activity;
import com.balanceflow.balanceflow_backend.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByGroupOrderByCreatedAtDesc(Group group);
    void deleteByGroup(Group group);
}
