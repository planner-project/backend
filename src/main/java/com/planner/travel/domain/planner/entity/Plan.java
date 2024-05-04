package com.planner.travel.domain.planner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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


