package com.balanceflow.balanceflow_backend.group.repository;

import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.group.entity.GroupMember;
import com.balanceflow.balanceflow_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

import java.util.Optional;
public interface GroupMemberRepository
        extends JpaRepository<GroupMember, UUID> {

    Optional<GroupMember> findByGroupAndUser(
            Group group,
            User user
    );

    List<GroupMember> findByUser(
            User user
    );
    List<GroupMember> findByGroup(Group group);
    void deleteByGroup(Group group);
}