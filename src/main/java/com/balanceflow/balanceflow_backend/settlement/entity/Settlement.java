package com.balanceflow.balanceflow_backend.settlement.entity;

import com.balanceflow.balanceflow_backend.group.entity.Group;
import com.balanceflow.balanceflow_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private User payer;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private Double amount;

    private LocalDateTime settledAt;
}
