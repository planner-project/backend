package com.planner.travel.domain.planner.entity;

import com.planner.travel.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private boolean isDeleted;
    private boolean isPrivate;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "plannerId")
    private Planner planner;

}
