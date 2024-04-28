package com.planner.travel.domain.group.entity;

import com.planner.travel.domain.planner.entity.PlanBox;
import com.planner.travel.domain.planner.entity.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long isLeaved;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "plannerId")
    private Planner plannerId;
}
