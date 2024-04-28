package com.planner.travel.domain.planner.entity;

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
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isPrivate;
    private String title;
    private LocalDateTime time;
    private String content;
    private String address;
    private boolean isDeleted;


    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "plannerBoxId")
    private PlanBox planBox;

}


